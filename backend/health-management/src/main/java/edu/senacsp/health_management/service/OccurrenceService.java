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

    private static final Duration BEFORE = Duration.ofHours(12).plusMinutes(5);
    private static final Duration AFTER = Duration.ofHours(12);

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

    @Transactional(readOnly = true)
    public List<OccurrenceResponse> findWindow(User user, Long profileId, ScheduleType type,
                                               Instant reference, Sort.Direction order) {
        // Validations
        if (profileId == null || profileId <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile ID is invalid");
        }

        if (!profileRepo.existsByIdAndUser(profileId, user))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }

        //-------------------------------------------------------------------------------------

        Instant now = Instant.now();
        Instant center = reference != null ? reference : now;
        Instant from = center.minus(BEFORE);
        Instant to = center.plus(AFTER);

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