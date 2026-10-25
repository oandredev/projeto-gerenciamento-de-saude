package edu.senacsp.health_management.service;

import edu.senacsp.health_management.dto.response.confirmation.ConfirmationResponse;
import edu.senacsp.health_management.dto.request.confirmation.CreateConfirmationRequest;

import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.entity.Schedule;
import edu.senacsp.health_management.entity.Confirmation;

import edu.senacsp.health_management.repository.ProfileRepository;
import edu.senacsp.health_management.repository.ScheduleRepository;
import edu.senacsp.health_management.repository.ConfirmationRepository;

import edu.senacsp.health_management.util.OccurrenceUtil;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ConfirmationService {

    /**
     * An occurrence can be confirmed or undone from 24 hours before until 24 hours after its
     * scheduled time. Both ends are exclusive: for 08:00, from 08:01 of the previous day to
     * 07:59 of the next day.
     */
    static final Duration WINDOW = Duration.ofHours(24);
    private static final Duration MAX_RANGE = Duration.ofDays(31);

    private final ConfirmationRepository repo;
    private final ScheduleRepository scheduleRepo;
    private final ProfileRepository profileRepo;
    private final OccurrenceUtil occurrenceUtil;

    public ConfirmationService(ConfirmationRepository repo, ScheduleRepository scheduleRepo, ProfileRepository profileRepo, OccurrenceUtil occurrenceUtil) {
        this.repo = repo;
        this.scheduleRepo = scheduleRepo;
        this.profileRepo = profileRepo;
        this.occurrenceUtil = occurrenceUtil;
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

        if (!occurrenceUtil.isOccurrence(schedule, scheduledAt))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Scheduled time is not an occurrence of this schedule");
        }

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
     * Lists the confirmations of one of the authenticated user's profiles whose confirmation time
     * is inside {@code [start, end)}, sorted by confirmation time.
     *
     * <p>The window cannot exceed {@code MAX_RANGE}. The profile is checked by ID and owner, so a
     * profile that does not exist and one from another user produce the same error.
     *
     * @param user      the authenticated user, resolved from the JWT
     * @param profileId the ID of the profile
     * @param start     the start of the window (inclusive)
     * @param end       the end of the window (exclusive)
     * @param order     the direction of the sort (ASC or DESC)
     * @return the confirmations in the window, empty if there are none
     * @throws ResponseStatusException if an ID or date is missing or invalid, or the window is
     *                                 inverted or too large (CODE 400)
     *                                 or the profile is not found for this user (CODE 404)
     */
    @Transactional(readOnly = true)
    public List<ConfirmationResponse> findByInterval(User user, Long profileId,
                                                       Instant start, Instant end, Sort.Direction order)
    {
        // Validations
        if (profileId == null || profileId <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile ID is invalid");
        }

        if (start == null || end == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start and End are required");
        }

        if (!start.isBefore(end))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start must be before end");
        }

        if (Duration.between(start, end).compareTo(MAX_RANGE) > 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Range cannot exceed " + MAX_RANGE.toDays() + " days");
        }

        // Validations (DB) | Checks if the specified profile actually exists and belongs to this user
        if (!profileRepo.existsByIdAndUser(profileId, user))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }

        // Return
        return repo.findAllByProfileBetween(profileId, start, end, Sort.by(order, "confirmedAt", "id"))
                .stream()
                .map(confirmation -> new ConfirmationResponse(
                        confirmation.getId(),
                        confirmation.getSchedule().getId(),
                        confirmation.getScheduledAt(),
                        confirmation.getConfirmedAt()))
                .toList();
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

    // VALIDATION

    private void validateConfirmationWindow(Instant scheduledAt, Instant now)
    {
        Instant opensAt = scheduledAt.minus(WINDOW);
        Instant closesAt = scheduledAt.plus(WINDOW);

        if (!now.isAfter(opensAt) || !now.isBefore(closesAt))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The confirmation window of this occurrence is closed or has not opened yet");
        }
    }
}