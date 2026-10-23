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

@Component
public class OccurrenceUtil
{
    public List<Instant> between(Schedule schedule, Instant from, Instant to)
    {
        Instant startAt = schedule.getStartAt().truncatedTo(ChronoUnit.SECONDS);
        Instant endAt = schedule.getEndAt() == null ? null : schedule.getEndAt().truncatedTo(ChronoUnit.SECONDS);
        IntervalUnit intervalUnit = schedule.getIntervalUnit();
        int intervalValue = schedule.getIntervalValue();

        List<Instant> list = new ArrayList<>();

        // A UNIQUE schedule has only one valid occurrence
        if (intervalUnit == IntervalUnit.UNIQUE)
        {
            boolean inWindow = !startAt.isBefore(from) && !startAt.isAfter(to);
            boolean beforeEnd = endAt == null || startAt.isBefore(endAt);

            if (inWindow && beforeEnd) list.add(startAt);
            return list;
        }

        if (intervalValue <= 0)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Schedule interval is not configured correctly");
        }

        // Jumps close to the window instead of iterating from startAt (2 of margin)
        long index = Math.max(0, calculateApproximateIndex(startAt, from, intervalUnit, intervalValue) - 2);

        for (; ; index++)
        {
            Instant occurrence = calculateOccurrence(startAt, intervalUnit, intervalValue, index);

            if (occurrence.isAfter(to) || (endAt != null && !occurrence.isBefore(endAt))) break;
            if (!occurrence.isBefore(from)) list.add(occurrence);
        }

        return list;
    }

    /** True if the time belongs to the rule: inside [startAt, endAt) and on its recurrence. */
    public boolean isOccurrence(Schedule schedule, Instant scheduledAt)
    {
        return between(schedule, scheduledAt, scheduledAt).contains(scheduledAt);
    }

    //-------------------------------------------------------------------------------------------------------------

    // Every occurrence is calculated from startAt (not from the previous one), which avoids month-end drift
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

    private long calculateApproximateIndex(Instant startAt, Instant target, IntervalUnit intervalUnit, int intervalValue)
    {
        ZonedDateTime start = startAt.atZone(ZoneOffset.UTC);
        ZonedDateTime end = target.atZone(ZoneOffset.UTC);

        long difference = switch (intervalUnit)
        {
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