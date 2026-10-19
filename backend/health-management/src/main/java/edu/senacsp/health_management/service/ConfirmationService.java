package edu.senacsp.health_management.service;

import edu.senacsp.health_management.dto.response.confirmation.ConfirmationResponse;
import edu.senacsp.health_management.dto.request.confirmation.CreateConfirmationRequest;

import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.entity.Schedule;
import edu.senacsp.health_management.entity.Confirmation;
import edu.senacsp.health_management.entity.enums.IntervalUnit;

import edu.senacsp.health_management.repository.ScheduleRepository;
import edu.senacsp.health_management.repository.ConfirmationRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.DateTimeException;
import java.time.temporal.ChronoUnit;

@Service
public class ConfirmationService {

    /**
     * An occurrence can be confirmed or undone from 24 hours before until 24 hours after its
     * scheduled time. Both ends are exclusive: for 08:00, from 08:01 of the previous day to
     * 07:59 of the next day.
     */
    private static final Duration WINDOW = Duration.ofHours(24);

    private final ConfirmationRepository repo;
    private final ScheduleRepository scheduleRepo;

    public ConfirmationService(ConfirmationRepository repo, ScheduleRepository scheduleRepo) {
        this.repo = repo;
        this.scheduleRepo = scheduleRepo;
    }

    /**
     * Confirms one occurrence of a schedule. If the row exists, the occurrence was fulfilled.
     *
     * <p>The schedule must be active and belong to the user (through its profile); an archived
     * schedule counts as deleted. Its row is locked, so editing the rule (PUT) and confirming
     * cannot run at the same time. The time must be a real occurrence of the rule and must be
     * inside its window (24 hours before until 24 hours after, both ends exclusive). An
     * occurrence can be confirmed only once.
     *
     * @param user the authenticated user, resolved from the JWT
     * @param req  the schedule and the occurrence to confirm
     * @return a {@link ConfirmationResponse} with the saved confirmation
     * @throws ResponseStatusException if a field is missing or invalid, or the time is not an
     *                                 occurrence of the schedule (CODE 400)
     *                                 or the active schedule is not found for this user (CODE 404)
     *                                 or the occurrence is outside its window, or it was already
     *                                 confirmed (CODE 409)
     */
    @Transactional
    public ConfirmationResponse create(User user, CreateConfirmationRequest req)
    {
        // Validations
        if (req.scheduleId() == null || req.scheduleId() <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule ID is invalid");
        }

        if (req.scheduleAt() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule At is required");
        }

        // Seconds precision, so the same instant always compares equal
        Instant scheduledAt = req.scheduleAt().truncatedTo(ChronoUnit.SECONDS);

        // Database: active schedule + owner in one query (single 404), row locked
        Schedule schedule = scheduleRepo
                .findActiveOwnedForUpdate(req.scheduleId(), user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Active schedule not found"));

        if (repo.existsBySchedule_IdAndScheduledAt(schedule.getId(), scheduledAt))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This schedule occurrence has already been confirmed");
        }

        validateOccurrence(schedule, scheduledAt);
        validateConfirmationWindow(scheduledAt, Instant.now());

        // Save and Return
        Confirmation confirmation = repo.save(new Confirmation(schedule, scheduledAt));

        return new ConfirmationResponse(
                confirmation.getId(),
                confirmation.getSchedule().getId(),
                confirmation.getScheduledAt(),
                confirmation.getConfirmedAt());
    }

    /**
     * Removes a confirmation (undo). Only allowed inside the occurrence window, like creating.
     *
     * @throws ResponseStatusException if the ID is invalid (CODE 400)
     *                                 or the confirmation is not found for this user, or its
     *                                 schedule is archived (CODE 404)
     *                                 or the occurrence window is closed (CODE 409)
     */
    @Transactional
    public void delete(User user, Long id) // No return, as there is nothing to return.
    {
        // Validations
        if (id == null || id <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Confirmation ID is invalid");
        }

        Long scheduleId = repo.findScheduleIdOwnedBy(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Confirmation not found"));

        scheduleRepo.findActiveOwnedForUpdate(scheduleId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Active schedule not found"));

        Confirmation confirmation = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Confirmation not found"));

        validateConfirmationWindow(confirmation.getScheduledAt(), Instant.now());

        // Delete
        repo.delete(confirmation);
    }

    //-------------------------------------------------------------------------------------------------------------

    // VALIDATIONS

    /** The time must belong to the rule: inside [startAt, endAt) and on its recurrence. */
    private void validateOccurrence(Schedule schedule, Instant scheduledAt)
    {
        Instant startAt = schedule.getStartAt().truncatedTo(ChronoUnit.SECONDS);
        Instant endAt = schedule.getEndAt() == null ? null : schedule.getEndAt().truncatedTo(ChronoUnit.SECONDS);

        if (scheduledAt.isBefore(startAt))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scheduled time cannot be before the schedule start");
        }

        // The end is exclusive, so a schedule that ends where another begins never overlaps it
        if (endAt != null && !scheduledAt.isBefore(endAt))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scheduled time must be before the schedule end");
        }

        IntervalUnit intervalUnit = schedule.getIntervalUnit();

        // A UNIQUE schedule has only one valid occurrence
        if (intervalUnit == IntervalUnit.UNIQUE)
        {
            if (!scheduledAt.equals(startAt))
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A unique schedule only has one occurrence");
            }

            return;
        }

        int intervalValue = schedule.getIntervalValue();

        if (intervalValue <= 0)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Schedule interval is not configured correctly");
        }

        /*
         * Find the occurrence index from the original startAt. Every occurrence is calculated
         * from startAt (not from the previous one), which avoids month-end drift.
         */
        long approximateIndex = calculateApproximateIndex(startAt, scheduledAt, intervalUnit, intervalValue);

        long firstIndex = Math.max(0, approximateIndex - 2);
        long lastIndex = approximateIndex + 2;

        for (long index = firstIndex; index <= lastIndex; index++)
        {
            if (calculateOccurrence(startAt, intervalUnit, intervalValue, index).equals(scheduledAt))
            {
                return;
            }
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scheduled time does not match the schedule recurrence");
    }

    /** Editable only strictly between (scheduledAt - 24h) and (scheduledAt + 24h). */
    private void validateConfirmationWindow(Instant scheduledAt, Instant now)
    {
        Instant opensAt = scheduledAt.minus(WINDOW);
        Instant closesAt = scheduledAt.plus(WINDOW);

        if (!now.isAfter(opensAt) || !now.isBefore(closesAt))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "The confirmation window of this occurrence is closed or has not opened yet");
        }
    }

    // OCCURRENCEs

    private Instant calculateOccurrence(Instant startAt, IntervalUnit intervalUnit, int intervalValue, long index)
    {
        try
        {
            long amount = Math.multiplyExact((long) intervalValue, index);

            ZonedDateTime start = startAt.atZone(ZoneOffset.UTC);

            ZonedDateTime occurrence = switch (intervalUnit)
            {
                case MINUTE -> start.plusMinutes(amount);
                case HOUR -> start.plusHours(amount);
                case DAY -> start.plusDays(amount);
                case WEEK -> start.plusWeeks(amount);
                case MONTH -> start.plusMonths(amount);
                case YEAR -> start.plusYears(amount);
                case UNIQUE -> start; // Never reached: UNIQUE returns before the loop
            };

            return occurrence.toInstant();
        }
        catch (ArithmeticException | DateTimeException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule interval is too large");
        }
    }

    private long calculateApproximateIndex(Instant startAt, Instant scheduledAt, IntervalUnit intervalUnit, int intervalValue)
    {
        ZonedDateTime start = startAt.atZone(ZoneOffset.UTC);
        ZonedDateTime target = scheduledAt.atZone(ZoneOffset.UTC);

        long difference = switch (intervalUnit)
        {
            case MINUTE -> Duration.between(start, target).toMinutes();
            case HOUR -> Duration.between(start, target).toHours();
            case DAY -> Duration.between(start, target).toDays();
            case WEEK -> Duration.between(start, target).toDays() / 7;
            case MONTH -> ChronoUnit.MONTHS.between(start, target);
            case YEAR -> ChronoUnit.YEARS.between(start, target);
            case UNIQUE -> 0;
        };

        return Math.max(0, difference / intervalValue);
    }
}