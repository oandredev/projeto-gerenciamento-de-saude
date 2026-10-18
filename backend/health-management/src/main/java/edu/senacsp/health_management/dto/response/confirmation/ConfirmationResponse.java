package edu.senacsp.health_management.dto.response.confirmation;

import java.time.Instant;

public record ConfirmationResponse(
        Long id,
        Long scheduleId,
        Instant scheduleAt,
        Instant confirmedAt
) {}