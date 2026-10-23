package edu.senacsp.health_management.dto.response.occurrence;

import edu.senacsp.health_management.entity.enums.OccurrenceStatus;
import edu.senacsp.health_management.entity.enums.ScheduleType;

import java.time.Instant;

public record OccurrenceResponse(
        Long confirmationId, // Used to UPDATE/DELETE
        Long scheduleId,
        String title,
        ScheduleType type,
        Instant scheduledAt,
        OccurrenceStatus status, // To facilitate  Frontend
        Instant confirmedAt
) {}