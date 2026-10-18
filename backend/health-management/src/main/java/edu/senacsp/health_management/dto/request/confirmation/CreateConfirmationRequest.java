package edu.senacsp.health_management.dto.request.confirmation;

import java.time.Instant;

/**
 * Request payload for creating a new Confirmation.
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *     "scheduleId": 8,
 *     "scheduleAt": "2026-10-19T19:00:00Z"
 * }
 * }</pre>
 *
 *  <p>Note: Valid times range from 00:00:00 to 23:59:59.999999999.
 *  The time 24:00:00 is not supported by Instant; use 00:00:00
 *  on the following day to represent midnight of the next day.
 *
 */
public record CreateConfirmationRequest(
        Long scheduleId,
        Instant scheduleAt
) {}