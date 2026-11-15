package edu.senacsp.health_management.util;

import edu.senacsp.health_management.entity.Schedule;

import edu.senacsp.health_management.entity.enums.IntervalUnit;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.util.List;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;

/**
 * Calculates the occurrences of a {@link Schedule} from its rule.
 *
 * <p>Occurrences are never stored. They come from {@code startAt}, the interval (value and
 * unit) and, optionally, {@code endAt}. Used to list the occurrences of a window
 * ({@code OccurrenceService}) and to check that a time to be confirmed really belongs to the
 * rule ({@code ConfirmationService}), so both always agree.
 *
 * <p>All calculations are done in UTC, with seconds precision. The component keeps no state.
 */
@Component
public class OccurrenceUtil {

    /**
     * Calculates the occurrences of a schedule inside the window {@code [from, to]}, in
     * ascending order. Both ends of the window are inclusive.
     *
     * <p>The rule starts at {@code startAt} (inclusive) and ends at {@code endAt} (exclusive);
     * a {@code null} {@code endAt} means it never ends. A UNIQUE schedule has a single
     * occurrence, at {@code startAt}, and it is returned only if it is inside the window.
     *
     * <p>Each occurrence is calculated from {@code startAt}, not from the previous one, so
     * errors do not accumulate. For months, a day that does not exist in the target month falls
     * on its last day (for example, the 31st becomes the 28th in February). The calculation does
     * not iterate from {@code startAt}: it jumps close to {@code from} and goes forward from
     * there, so a schedule that started long ago stays cheap.
     *
     * @param schedule the schedule whose rule is used
     * @param from     the start of the window (inclusive)
     * @param to       the end of the window (inclusive)
     * @return the occurrences in the window, empty if there are none
     * @throws ResponseStatusException if the interval value of a recurring schedule is not
     *                                 greater than zero (CODE 500)
     *                                 or the interval is so large that the date overflows (CODE 400)
     */
    public List<Instant> between(Schedule schedule, Instant from, Instant to) {
        Instant startAt = schedule.getStartAt().truncatedTo(ChronoUnit.SECONDS);
        Instant endAt = schedule.getEndAt() == null ? null : schedule.getEndAt().truncatedTo(ChronoUnit.SECONDS);
        IntervalUnit intervalUnit = schedule.getIntervalUnit();
        int intervalValue = schedule.getIntervalValue();

        List<Instant> list = new ArrayList<>();

        // A UNIQUE schedule has only one valid occurrence
        if (intervalUnit == IntervalUnit.UNIQUE) {
            boolean inWindow = !startAt.isBefore(from) && !startAt.isAfter(to);
            boolean beforeEnd = endAt == null || startAt.isBefore(endAt);

            if (inWindow && beforeEnd) {
                list.add(startAt);
            }
            return list;
        }

        if (intervalValue <= 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Schedule interval is not configured correctly");
        }

        // Jumps close to the window instead of iterating from startAt (2 of margin)
        long index = Math.max(0, calculateApproximateIndex(startAt, from, intervalUnit, intervalValue) - 2);

        for (; ; index++) {
            Instant occurrence = calculateOccurrence(startAt, intervalUnit, intervalValue, index);

            if (occurrence.isAfter(to) || (endAt != null && !occurrence.isBefore(endAt))) {
                break;
            }

            if (!occurrence.isBefore(from)) {
                list.add(occurrence);
            }
        }

        return list;
    }

    /**
     * Tells whether a time is an occurrence of the schedule: inside {@code [startAt, endAt)} and
     * exactly on its recurrence.
     *
     * <p>The time must have seconds precision, like the calculated occurrences; otherwise it
     * never matches.
     *
     * @param schedule    the schedule whose rule is used
     * @param scheduledAt the time to check
     * @return {@code true} if the time is an occurrence of the schedule
     * @throws ResponseStatusException as in {@link #between(Schedule, Instant, Instant)}
     */
    public boolean isOccurrence(Schedule schedule, Instant scheduledAt) {
        return between(schedule, scheduledAt, scheduledAt).contains(scheduledAt);
    }

    //-------------------------------------------------------------------------------------------------------------

    // Every occurrence is calculated from startAt (not from the previous one), which avoids month-end drift.
    // Overflow of the amount or of the date is reported as a 400
    private Instant calculateOccurrence(Instant startAt, IntervalUnit intervalUnit, int intervalValue, long index) {
        try {
            long amount = Math.multiplyExact((long) intervalValue, index);

            ZonedDateTime start = startAt.atZone(ZoneOffset.UTC);

            ZonedDateTime occurrence = switch (intervalUnit) {
                case MINUTE -> start.plusMinutes(amount);
                case HOUR -> start.plusHours(amount);
                case DAY -> start.plusDays(amount);
                case WEEK -> start.plusWeeks(amount);
                case MONTH -> start.plusMonths(amount);
                case YEAR -> start.plusYears(amount);
                case UNIQUE -> start; // Never reached: UNIQUE returns before the loop
            };

            return occurrence.toInstant();
        } catch (ArithmeticException | DateTimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule interval is too large");
        }
    }

    // Lower bound for the index of the first occurrence at or after the target: the occurrence
    // at this index is never after the target, and is at most about one step before the one
    // needed (months can be a little further, since month-end days are adjusted). The caller
    // steps back 2 more as a margin and filters by the window
    private long calculateApproximateIndex(Instant startAt, Instant target, IntervalUnit intervalUnit, int intervalValue) {
        ZonedDateTime start = startAt.atZone(ZoneOffset.UTC);
        ZonedDateTime end = target.atZone(ZoneOffset.UTC);

        long difference = switch (intervalUnit) {
            case MINUTE -> Duration.between(start, end).toMinutes();
            case HOUR -> Duration.between(start, end).toHours();
            case DAY -> Duration.between(start, end).toDays();
            case WEEK -> Duration.between(start, end).toDays() / 7;
            case MONTH -> ChronoUnit.MONTHS.between(start, end);
            case YEAR -> ChronoUnit.YEARS.between(start, end);
            case UNIQUE -> 0;
        };

        return Math.max(0, difference / intervalValue);
    }
}
