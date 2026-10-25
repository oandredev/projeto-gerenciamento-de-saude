package edu.senacsp.health_management.service;

import edu.senacsp.health_management.dto.response.occurrence.OccurrenceResponse;

import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.entity.Schedule;
import edu.senacsp.health_management.entity.Confirmation;

import edu.senacsp.health_management.entity.enums.ScheduleType;
import edu.senacsp.health_management.entity.enums.OccurrenceStatus;

import edu.senacsp.health_management.repository.ProfileRepository;
import edu.senacsp.health_management.repository.ScheduleRepository;
import edu.senacsp.health_management.repository.ConfirmationRepository;

import edu.senacsp.health_management.util.OccurrenceUtil;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OccurrenceService {

    // OccurrenceService
    private static final Duration BEFORE = ConfirmationService.WINDOW.minusHours(1);
    private static final Duration AFTER = ConfirmationService.WINDOW.minusHours(1);

    private static final Duration CLOCK_TOLERANCE = Duration.ofMinutes(30);

    private final ScheduleRepository scheduleRepo;
    private final ConfirmationRepository confirmationRepo;
    private final ProfileRepository profileRepo;

    private final OccurrenceUtil occurrenceUtil;

    public OccurrenceService(ScheduleRepository scheduleRepo, ConfirmationRepository confirmationRepo, ProfileRepository profileRepo, OccurrenceUtil occurrenceUtil) {
        this.scheduleRepo = scheduleRepo;
        this.confirmationRepo = confirmationRepo;
        this.profileRepo = profileRepo;
        this.occurrenceUtil = occurrenceUtil;
    }

    /**
     * Lists the occurrences of one of the authenticated user's profiles inside the window around
     * now (24 hours before until 24 hours after), crossed with the confirmations.
     *
     * <p>Occurrences are not stored: they are calculated from each active schedule's rule. Each one
     * is {@code CONFIRMED} if a confirmation exists for it, otherwise {@code PENDING} if its time
     * has passed or {@code SCHEDULE} if it is still to come.
     *
     * <p>The client sends its own current time in {@code reference}, which must be the same instant
     * as the server's, within {@code CLOCK_TOLERANCE}. A bigger gap means a time zone mistake (local
     * time sent as if it were UTC) or a wrong device clock, so the request is refused. The window is
     * always calculated around the server's clock.
     *
     * @param user      the authenticated user, resolved from the JWT
     * @param profileId the ID of the profile
     * @param type      the type to filter by, or {@code null} for every type
     * @param reference the device's current time (UTC)
     * @param order     the direction of the sort by occurrence time (ASC or DESC)
     * @return the occurrences in the window, empty if there are none
     * @throws ResponseStatusException if the profile ID is invalid or the client time does not match
     *                                 the server time (CODE 400)
     *                                 or the profile is not found for this user (CODE 404)
     */
    @Transactional(readOnly = true)
    public List<OccurrenceResponse> findWindow(User user, Long profileId, ScheduleType type,
                                               Instant reference, Sort.Direction order) {
        // Validations
        if (profileId == null || profileId <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile ID is invalid");
        }

        Instant now = Instant.now();

        // Both are instants, so there is nothing to convert: just compare
        if (reference == null || Duration.between(now, reference).abs().compareTo(CLOCK_TOLERANCE) > 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Client time does not match the server time: send instants in UTC (ISO-8601 with Z) and check the device clock");
        }

        if (!profileRepo.existsByIdAndUser(profileId, user))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }

        //-------------------------------------------------------------------------------------

        Instant from = now.minus(BEFORE);
        Instant to = now.plus(AFTER);

        List<Schedule> schedules = scheduleRepo
                .findActiveInWindow(user.getId(), profileId, type, from, to);

        if (schedules.isEmpty()) return List.of();

        List<Long> ids = schedules.stream().map(Schedule::getId).toList();

        Map<String, Confirmation> confirmed = confirmationRepo
                .findInWindow(ids, from, to)
                .stream()
                .collect(Collectors.toMap(
                        c -> c.getSchedule().getId() + "|" + c.getScheduledAt(),
                        Function.identity()));

        List<OccurrenceResponse> result = new ArrayList<>();
        for (Schedule s : schedules) {
            for (Instant at : occurrenceUtil.between(s, from, to)) {
                Confirmation c = confirmed.get(s.getId() + "|" + at);
                OccurrenceStatus status = c != null ? OccurrenceStatus.CONFIRMED
                        : at.isAfter(now) ? OccurrenceStatus.SCHEDULE
                        : OccurrenceStatus.PENDING;

                result.add(new OccurrenceResponse(
                        c != null ? c.getId() : null,
                        s.getId(),
                        s.getTitle(),
                        s.getType(),
                        at,
                        status,
                        c != null ? c.getConfirmedAt() : null));
            }
        }

        Comparator<OccurrenceResponse> cmp = Comparator.comparing(OccurrenceResponse::scheduledAt);
        result.sort(order.isAscending() ? cmp : cmp.reversed());

        //-------------------------------------------------------------------------------------

        return result;
    }
}