package edu.senacsp.health_management.service;

import edu.senacsp.health_management.dto.request.schedule.ScheduleDetails;
import edu.senacsp.health_management.dto.request.schedule.CreateMedicationData;
import edu.senacsp.health_management.dto.request.schedule.CreateAppointmentData;
import edu.senacsp.health_management.dto.request.schedule.CreateScheduleRequest;
import edu.senacsp.health_management.dto.request.schedule.UpdateScheduleRequest;
import edu.senacsp.health_management.dto.response.schedule.MedicationDto;
import edu.senacsp.health_management.dto.response.schedule.AppointmentDto;
import edu.senacsp.health_management.dto.response.schedule.ScheduleResponse;
import edu.senacsp.health_management.dto.response.schedule.ExtraScheduleDetails;

import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.Schedule;
import edu.senacsp.health_management.entity.MedicationSchedule;
import edu.senacsp.health_management.entity.AppointmentSchedule;

import edu.senacsp.health_management.entity.enums.DosageUnit;
import edu.senacsp.health_management.entity.enums.IntervalUnit;
import edu.senacsp.health_management.entity.enums.ReminderUnit;
import edu.senacsp.health_management.entity.enums.ScheduleType;
import edu.senacsp.health_management.entity.enums.MedicationStripe;

import edu.senacsp.health_management.repository.*;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
    private final ConfirmationRepository confirmationRepo;

    private static final int TITLE_MIN = 3;
    private static final int TITLE_MAX = 100;
    private static final int NOTES_MAX = 1000;
    private static final int TEXT_MAX = 255;
    private static final Double DOSAGE_MAX = 10000d; // TODO | Verify

    // Letters, digits, spaces and common punctuation
    private static final Pattern TEXT_PATTERN = Pattern.compile("^[\\p{L}\\p{Nd}][\\p{L}\\p{Nd} .,;:!?()'/+%\\-]*$");

    public ScheduleService(ScheduleRepository repo, MedicationScheduleRepository medicationRepo, AppointmentScheduleRepository appointmentRepo, ProfileRepository profileRepo, ConfirmationRepository confirmationRepo) {
        this.repo = repo;
        this.medicationRepo = medicationRepo;
        this.appointmentRepo = appointmentRepo;
        this.profileRepo = profileRepo;
        this.confirmationRepo = confirmationRepo;
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

        RuleValues v = validateRule(req.type(), req.title(), req.startAt(), req.intervalUnit(), req.intervalValue(),
                req.endAt(), req.reminderValue(), req.reminderUnit(), req.notes(), req.details());

        Profile profile = profileRepo.findByIdAndUser(req.profileId(), user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        //-------------------------------------------------------------------------------------

        return saveNew(profile, req.type(), v, req.reminderValue(), req.reminderUnit(),
                req.notifyEnabled() == null || req.notifyEnabled(),
                req.alarmEnabled() != null && req.alarmEnabled());
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

    /**
     * Updates a schedule of one of the authenticated user's profiles. Like a PUT, the request
     * replaces the whole rule and its type-specific data: optional fields that are not sent
     * ({@code notes}, {@code endAt}, the reminder, brand, active ingredient, photo, doctor and
     * location) are cleared, and a missing {@code stripe} goes back to NONE. Only
     * {@code notifyEnabled} and {@code alarmEnabled} keep their current value when {@code null}.
     *
     * <p>The active schedule is looked up by ID and owner, and its row is locked, so confirming
     * and editing cannot run at the same time. The {@code type} cannot be changed.
     *
     * <p>If the schedule has no confirmations, it is edited in place (the schedule and its
     * complementary row). If it has, the history must keep its meaning: the schedule is archived
     * ({@code active = false}) and a new, independent one is created with the new data, whatever
     * the dates sent. The returned {@code id} is then the new schedule's, so the client must replace
     * the one it had.
     *
     * @param user the authenticated user, resolved from the JWT
     * @param req  the schedule ID and its new data
     * @return a {@link ScheduleResponse} with the updated (or newly created) schedule
     * @throws ResponseStatusException if any field is missing or invalid, or the type changed (CODE 400)
     *                                 or the active schedule is not found for this user (CODE 404)
     */
    @Transactional
    public ScheduleResponse update(User user, UpdateScheduleRequest req)
    {
        // Validations
        if (req.id() == null || req.id() <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule ID is invalid");
        }

        RuleValues v = validateRule(req.type(), req.title(), req.startAt(), req.intervalUnit(), req.intervalValue(),
                req.endAt(), req.reminderValue(), req.reminderUnit(), req.notes(), req.details());

        // Database: active schedule + owner in one query (single 404), row locked
        Schedule old = repo.findActiveOwnedForUpdate(req.id(), user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Active schedule not found"));

        if (old.getType() != req.type())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type cannot be changed");
        }

        // null = keep the current value
        boolean notify = req.notifyEnabled() != null ? req.notifyEnabled() : old.isNotifyEnabled();
        boolean alarm = req.alarmEnabled() != null ? req.alarmEnabled() : old.isAlarmEnabled();

        //-------------------------------------------------------------------------------------

        // With history: the old rule is archived and a new, independent one is created
        if (confirmationRepo.existsBySchedule_Id(old.getId()))
        {
            old.setActive(false);

            return saveNew(old.getProfile(), old.getType(), v, req.reminderValue(), req.reminderUnit(), notify, alarm);
        }

        //-------------------------------------------------------------------------------------

        // No history: edit in place (managed entities are saved by dirty checking)
        old.setTitle(v.title());
        old.setStartAt(v.startAt());
        old.setIntervalValue(v.intervalValue());
        old.setIntervalUnit(v.intervalUnit());
        old.setEndAt(v.endAt());
        old.setNotes(v.notes());
        old.setReminderValue(req.reminderValue());
        old.setReminderUnit(req.reminderUnit());
        old.setNotifyEnabled(notify);
        old.setAlarmEnabled(alarm);

        ExtraScheduleDetails details = null;

        if (v.medication() != null)
        {
            // The complementary row shares its ID with the schedule (@MapsId)
            MedicationSchedule row = medicationRepo.findById(old.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Medication data not found"));

            row.setMedicineName(v.medication().medicineName());
            row.setDosageAmount(v.medication().dosageAmount());
            row.setDosageUnit(v.medication().dosageUnit());
            row.setBrand(v.medication().brand());
            row.setActiveIngredient(v.medication().activeIngredient());
            row.setStripe(v.medication().stripe());
            row.setPhotoUrl(v.medication().photoUrl());

            details = new MedicationDto(row);
        }
        else if (v.appointment() != null)
        {
            AppointmentSchedule row = appointmentRepo.findById(old.getId())
                    .orElseGet(() -> appointmentRepo.save(new AppointmentSchedule(old)));

            row.setDoctorName(v.appointment().doctorName());
            row.setLocation(v.appointment().location());

            details = new AppointmentDto(row);
        }
        repo.flush();

        return new ScheduleResponse(old, details);
    }

    /**
     * Archives a schedule (soft delete): sets {@code active = false}. Its confirmations stay as
     * history, but the schedule no longer appears in the occurrences or in the schedule list, and
     * its confirmations can no longer be created or undone.
     *
     * <p>The active schedule is looked up by ID and owner, and its row is locked. Archiving an
     * already archived schedule gives the same error as one that does not exist.
     *
     * @param user the authenticated user, resolved from the JWT
     * @param id   the schedule ID
     * @throws ResponseStatusException if the ID is invalid (CODE 400)
     *                                 or the active schedule is not found for this user (CODE 404)
     */
    @Transactional
    public void delete(User user, Long id)
    {
        // Validations
        if (id == null || id <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule ID is invalid");
        }

        // Database: active schedule + owner in one query (single 404), row locked
        Schedule schedule = repo.findActiveOwnedForUpdate(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Active schedule not found"));

        // Soft delete: confirmations stay as history
        schedule.setActive(false);
    }

    //-----------------------------------------------------

    // SAVE

    /** Saves a new schedule and its complementary row. Used by create and by the versioning in update. */
    private ScheduleResponse saveNew(Profile profile, ScheduleType type, RuleValues v,
                                     Integer reminderValue, ReminderUnit reminderUnit,
                                     boolean notifyEnabled, boolean alarmEnabled)
    {
        Schedule schedule = new Schedule(profile, type, v.title(), v.startAt(),
                v.intervalValue(), v.intervalUnit(), v.endAt());

        schedule.setNotes(v.notes());
        schedule.setReminderValue(reminderValue);
        schedule.setReminderUnit(reminderUnit);
        schedule.setNotifyEnabled(notifyEnabled);
        schedule.setAlarmEnabled(alarmEnabled);

        // Save (it is saved twice: the schedule first, then its complementary row, depending on the type)
        schedule = repo.save(schedule);

        ExtraScheduleDetails details = null;

        if (v.medication() != null)
        {
            MedicationValues m = v.medication();

            MedicationSchedule row = new MedicationSchedule(schedule, m.medicineName(), m.dosageAmount(), m.dosageUnit());
            row.setBrand(m.brand());
            row.setActiveIngredient(m.activeIngredient());
            row.setStripe(m.stripe());
            row.setPhotoUrl(m.photoUrl());

            details = new MedicationDto(medicationRepo.save(row));
        }
        else if (v.appointment() != null)
        {
            AppointmentSchedule row = new AppointmentSchedule(schedule);
            row.setDoctorName(v.appointment().doctorName());
            row.setLocation(v.appointment().location());

            details = new AppointmentDto(appointmentRepo.save(row));
        }

        return new ScheduleResponse(schedule, details);
    }

    //-----------------------------------------------------

    // VALIDATIONS
    private record MedicationValues(String brand, String medicineName, String activeIngredient, Double dosageAmount, DosageUnit dosageUnit, MedicationStripe stripe, String photoUrl) { }

    private record AppointmentValues(String doctorName, String location) { }

    private record RuleValues(String title, Instant startAt, int intervalValue, IntervalUnit intervalUnit, Instant endAt, String notes, MedicationValues medication, AppointmentValues appointment) { }

    /** Validates what create and update have in common. Start and end come back with seconds precision. */
    private RuleValues validateRule(ScheduleType type, String title, Instant startAt, IntervalUnit intervalUnit,
                                    Integer intervalValue, Instant endAt, Integer reminderValue,
                                    ReminderUnit reminderUnit, String notes, ScheduleDetails details)
    {
        if (type == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type is required");
        }

        String validTitle = validateTitle(title);

        if (startAt == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start is required");
        }

        if (intervalUnit == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Interval unit is required");
        }

        Instant start = startAt.truncatedTo(ChronoUnit.SECONDS);
        int value = 0;
        Instant end = null;

        if (intervalUnit != IntervalUnit.UNIQUE)
        {
            if (intervalValue == null || intervalValue <= 0)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Interval value must be greater than zero");
            }

            value = intervalValue;
            end = endAt == null ? null : endAt.truncatedTo(ChronoUnit.SECONDS);

            if (end != null && !end.isAfter(start))
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End must be after start");
            }
        }

        if ((reminderValue == null) != (reminderUnit == null))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reminder value and unit must be sent together");
        }

        if (reminderValue != null && reminderValue <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reminder value must be greater than zero");
        }

        String validNotes = optionalText(notes, NOTES_MAX, "Notes");

        MedicationValues medication = null;
        AppointmentValues appointment = null;

        switch (type)
        {
            case MEDICATION ->
            {
                if (!(details instanceof CreateMedicationData data))
                {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medication data is required");
                }

                medication = validateMedication(data);
            }
            case CONSULTATION, EXAM ->
            {
                if (details != null && !(details instanceof CreateAppointmentData))
                {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Details do not match the type");
                }

                appointment = validateAppointment((CreateAppointmentData) details);
            }
            default ->
            {
                if (details != null)
                {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This type does not take extra data");
                }
            }
        }

        return new RuleValues(validTitle, start, value, intervalUnit, end, validNotes, medication, appointment);
    }

    private MedicationValues validateMedication(CreateMedicationData data) {
        if (data.medicineName() == null || data.medicineName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medicine name is required");
        }

        String medicineName = data.medicineName().trim().replaceAll("\\s+", " ");

        if (medicineName.length() > TEXT_MAX || !TEXT_PATTERN.matcher(medicineName).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medicine name is invalid");
        }

        Double amount = data.dosageAmount();

        if (amount == null
                || !Double.isFinite(amount)
                || amount <= 0
                || amount > DOSAGE_MAX
                || BigDecimal.valueOf(amount).stripTrailingZeros().scale() > 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dosage amount is invalid");
        }

        if (data.dosageUnit() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dosage unit is required");
        }

        return new MedicationValues(
                optionalText(data.brand(), TEXT_MAX, "Brand"),
                medicineName,
                optionalText(data.activeIngredient(), TEXT_MAX, "Active ingredient"),
                amount,
                data.dosageUnit(),
                data.stripe() == null ? MedicationStripe.NONE : data.stripe(),
                optionalText(data.photoUrl(), TEXT_MAX, "Photo URL")
        );
    }

    private AppointmentValues validateAppointment(CreateAppointmentData data) {
        if (data == null) {
            return new AppointmentValues(null, null);
        }

        return new AppointmentValues(
                optionalText(data.doctorName(), TEXT_MAX, "Doctor name"),
                optionalText(data.location(), TEXT_MAX, "Location"));
    }

    private String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required");
        }

        String t = title.trim().replaceAll("\\s+", " ");

        if (t.length() < TITLE_MIN || t.length() > TITLE_MAX) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title must have between " + TITLE_MIN + " and " + TITLE_MAX + " characters");
        }

        if (!TEXT_PATTERN.matcher(t).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title contains invalid characters");
        }

        return t;
    }

    private String optionalText(String value, int max, String field) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String v = value.trim();

        if (v.length() > max) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " must have at most " + max + " characters");
        }

        return v;
    }
}