package edu.senacsp.health_management.service;

import edu.senacsp.health_management.dto.request.schedule.CreateAppointmentData;
import edu.senacsp.health_management.dto.request.schedule.CreateMedicationData;
import edu.senacsp.health_management.dto.request.schedule.CreateScheduleRequest;
import edu.senacsp.health_management.dto.response.schedule.ExtraScheduleDetails;
import edu.senacsp.health_management.dto.response.schedule.ScheduleResponse;
import edu.senacsp.health_management.dto.response.schedule.AppointmentDto;
import edu.senacsp.health_management.dto.response.schedule.MedicationDto;

import edu.senacsp.health_management.entity.AppointmentSchedule;
import edu.senacsp.health_management.entity.MedicationSchedule;
import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.Schedule;
import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.entity.enums.DosageUnit;
import edu.senacsp.health_management.entity.enums.IntervalUnit;
import edu.senacsp.health_management.entity.enums.MedicationStripe;

import edu.senacsp.health_management.entity.enums.ScheduleType;

import edu.senacsp.health_management.repository.AppointmentScheduleRepository;
import edu.senacsp.health_management.repository.MedicationScheduleRepository;
import edu.senacsp.health_management.repository.ProfileRepository;
import edu.senacsp.health_management.repository.ScheduleRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.Instant;
import java.math.BigDecimal;
import java.util.regex.Pattern;
import java.time.temporal.ChronoUnit;

@Service
public class ScheduleService {

    private final ScheduleRepository repo;
    private final MedicationScheduleRepository medicationRepo;
    private final AppointmentScheduleRepository appointmentRepo;
    private final ProfileRepository profileRepo;

    private static final int TITLE_MIN = 3;
    private static final int TITLE_MAX = 100;
    private static final int NOTES_MAX = 1000;
    private static final int TEXT_MAX = 255;
    private static final Double DOSAGE_MAX = 1000d; // TODO Verify

    // Letters, digits, spaces and common punctuation
    private static final Pattern TEXT_PATTERN =
            Pattern.compile("^[\\p{L}\\p{Nd}][\\p{L}\\p{Nd} .,;:!?()'/+%\\-]*$");

    public ScheduleService(ScheduleRepository repo,
                           MedicationScheduleRepository medicationRepo,
                           AppointmentScheduleRepository appointmentRepo,
                           ProfileRepository profileRepo) {
        this.repo = repo;
        this.medicationRepo = medicationRepo;
        this.appointmentRepo = appointmentRepo;
        this.profileRepo = profileRepo;
    }

    /**
     * Creates a schedule (and its type-specific data) for one of the authenticated user's profiles.
     *
     * <p>The schedule and its complementary row are saved in the same transaction, so a failure
     * never leaves a MEDICATION schedule without its medication data. The profile is looked up
     * by ID <em>and</em> owner, so a profile that does not exist and one that belongs to another
     * user produce the same error.
     *
     * <p>The {@code details} block must match the {@code type}: medication data is required for
     * MEDICATION, appointment data is optional for CONSULTATION and EXAM, and the other types
     * take none. For UNIQUE schedules, the interval value and the end are ignored (stored as 0
     * and {@code null}). {@code startAt} may be in the past; the client is expected to warn.
     *
     * @param user the authenticated user, resolved from the JWT
     * @param req  the schedule data and its type-specific details
     * @return a {@link ScheduleResponse} with the created schedule
     * @throws ResponseStatusException if any field is missing or invalid, or the details do not
     *                                 match the type (CODE 400)
     *                                 or the profile is not found for this user (CODE 404)
     */
    @Transactional
    public ScheduleResponse create(User user, CreateScheduleRequest req)
    {
        // Validations

        if (req.profileId() == null || req.profileId() <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile ID is invalid");
        }

        if (req.type() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type is required");
        }

        String title = validateTitle(req.title());

        if (req.startAt() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start is required");
        }

        if (req.intervalUnit() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Interval unit is required");
        }

        int intervalValue = 0;
        Instant endAt = null;

        if (req.intervalUnit() != IntervalUnit.UNIQUE)
        {
            if (req.intervalValue() == null || req.intervalValue() <= 0)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Interval value must be greater than zero");
            }

            intervalValue = req.intervalValue();
            endAt = req.endAt();

            if (endAt != null && !endAt.isAfter(req.startAt()))
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End must be after start");
            }
        }

        if ((req.reminderValue() == null) != (req.reminderUnit() == null))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reminder value and unit must be sent together");
        }

        if (req.reminderValue() != null && req.reminderValue() <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reminder value must be greater than zero");
        }

        String notes = optionalText(req.notes(), NOTES_MAX, "Notes");

        MedicationValues medication = null;
        AppointmentValues appointment = null;

        switch (req.type())
        {
            case MEDICATION ->
            {
                if (!(req.details() instanceof CreateMedicationData data))
                {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medication data is required");
                }

                medication = validateMedication(data);
            }
            case CONSULTATION, EXAM ->
            {
                if (req.details() != null && !(req.details() instanceof CreateAppointmentData))
                {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Details do not match the type");
                }

                appointment = validateAppointment((CreateAppointmentData) req.details());
            }
            default ->
            {
                if (req.details() != null)
                {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This type does not take extra data");
                }
            }
        }

        Profile profile = profileRepo.findByIdAndUser(req.profileId(), user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        Schedule schedule = new Schedule(profile, req.type(), title,
                req.startAt().truncatedTo(ChronoUnit.SECONDS), intervalValue, req.intervalUnit(),
                endAt == null ? null : endAt.truncatedTo(ChronoUnit.SECONDS));

        schedule.setNotes(notes);
        schedule.setReminderValue(req.reminderValue());
        schedule.setReminderUnit(req.reminderUnit());

        if (req.notifyEnabled() != null)
        {
            schedule.setNotifyEnabled(req.notifyEnabled());
        }

        if (req.alarmEnabled() != null)
        {
            schedule.setAlarmEnabled(req.alarmEnabled());
        }

        // Save (3 ways to save; it will always be saved twice: once upon scheduling and an additional time, depending on the type)
        schedule = repo.save(schedule);

        ExtraScheduleDetails details = null;

        if (medication != null)
        {
            MedicationSchedule m = new MedicationSchedule(schedule, medication.medicineName(),
                    medication.dosageAmount(), medication.dosageUnit());
            m.setBrand(medication.brand());
            m.setActiveIngredient(medication.activeIngredient());
            m.setStripe(medication.stripe());
            // Save
            details = new MedicationDto(medicationRepo.save(m));
        }
        else if (appointment != null)
        {
            AppointmentSchedule a = new AppointmentSchedule(schedule);
            a.setDoctorName(appointment.doctorName());
            a.setLocation(appointment.location());

            // Save
            details = new AppointmentDto(appointmentRepo.save(a));
        }

        return new ScheduleResponse(schedule, details);
    }

    /**
     * Lists the active schedules of one of the authenticated user's profiles, each one with its
     * type-specific details, optionally filtered by type and sorted by start date.
     *
     * <p>The profile is checked first, by ID <em>and</em> owner, so a profile that does not
     * exist and one that belongs to another user produce the same error, which avoids revealing
     * which IDs exist. Only after that are the schedules loaded, together with their complementary
     * rows, in a single query. Archived schedules ({@code active = false}) count as deleted and
     * are not returned. A profile of this user that simply has no schedules returns an empty list.
     *
     * <p>When {@code type} is {@code null} there is no type filter and every type is returned.
     * The order is decided by the database: {@code startAt} in the requested direction, with the
     * schedule ID as a tie-breaker in the same direction, so the result is always stable.
     *
     * <p>The {@code details} of each item depend on its {@code type}: medication data for
     * MEDICATION, appointment data for CONSULTATION or EXAM, and {@code null} for the other types
     * (or for an appointment that has no data).
     *
     * @param user      the authenticated user, resolved from the JWT
     * @param profileId the ID of the profile whose schedules are listed
     * @param type      the type to filter by, or {@code null} for every type
     * @param order     the direction of the sort by start date (ASC or DESC); must not be {@code null}
     * @return the profile's active schedules with their details (empty list if none)
     * @throws ResponseStatusException if the profile ID is missing or invalid (CODE 400)
     *                                 or the profile is not found for this user (CODE 404)
     */
    @Transactional(readOnly = true)
    public List<ScheduleResponse> findAllByProfile(User user, Long profileId, ScheduleType type, Sort.Direction order)
    {
        // Validations
        if (profileId == null || profileId <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile ID is invalid");
        }

        // Validations (DB) | Checks if the specified profile actually exists and belongs to this user
        Profile profile = profileRepo.findByIdAndUser(profileId, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        // null = no filter
        Collection<ScheduleType> types = type == null ? EnumSet.allOf(ScheduleType.class) : EnumSet.of(type);

        Sort sort = Sort.by(order, "startAt", "id");

        // TODO: if a new details DTO is added to Schedule, update this (and the repository query)
        // Return
        return repo.findAllWithDetailsByProfile(profile, types, sort)
                .stream()
                .map(row -> {
                    Schedule s = (Schedule) row[0];

                    MedicationSchedule m = (MedicationSchedule) row[1];
                    AppointmentSchedule a = (AppointmentSchedule) row[2];

                    ExtraScheduleDetails details = m != null ? new MedicationDto(m) : a != null ? new AppointmentDto(a) : null;

                    return new ScheduleResponse(s, details);
                })
                .toList();
    }
    //-----------------------------------------------------

    // VALIDATIONS
    private record MedicationValues(String brand, String medicineName, String activeIngredient, Double dosageAmount, DosageUnit dosageUnit, MedicationStripe stripe) {}
    private record AppointmentValues(String doctorName, String location) {}

    private MedicationValues validateMedication(CreateMedicationData data)
    {
        if (data.medicineName() == null || data.medicineName().isBlank())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medicine name is required");
        }

        String medicineName = data.medicineName().trim().replaceAll("\\s+", " ");

        if (medicineName.length() > TEXT_MAX || !TEXT_PATTERN.matcher(medicineName).matches())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medicine name is invalid");
        }

        Double amount = data.dosageAmount();

        if (amount == null
                || !Double.isFinite(amount)
                || amount <= 0
                || amount > DOSAGE_MAX
                || BigDecimal.valueOf(amount).stripTrailingZeros().scale() > 3)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dosage amount is invalid");
        }

        if (data.dosageUnit() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dosage unit is required");
        }

        return new MedicationValues(
                optionalText(data.brand(), TEXT_MAX, "Brand"),
                medicineName,
                optionalText(data.activeIngredient(), TEXT_MAX, "Active ingredient"),
                amount,
                data.dosageUnit(),
                data.stripe() == null ? MedicationStripe.NONE : data.stripe()
        );
    }

    private AppointmentValues validateAppointment(CreateAppointmentData data)
    {
        if (data == null)
        {
            return new AppointmentValues(null, null);
        }

        return new AppointmentValues(
                optionalText(data.doctorName(), TEXT_MAX, "Doctor name"),
                optionalText(data.location(), TEXT_MAX, "Location"));
    }

    private String validateTitle(String title)
    {
        if (title == null || title.isBlank())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required");
        }

        String t = title.trim().replaceAll("\\s+", " ");

        if (t.length() < TITLE_MIN || t.length() > TITLE_MAX)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title must have between " + TITLE_MIN + " and " + TITLE_MAX + " characters");
        }

        if (!TEXT_PATTERN.matcher(t).matches())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title contains invalid characters");
        }

        return t;
    }

    private String optionalText(String value, int max, String field)
    {
        if (value == null || value.isBlank())
        {
            return null;
        }

        String v = value.trim();

        if (v.length() > max)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " must have at most " + max + " characters");
        }

        return v;
    }
}