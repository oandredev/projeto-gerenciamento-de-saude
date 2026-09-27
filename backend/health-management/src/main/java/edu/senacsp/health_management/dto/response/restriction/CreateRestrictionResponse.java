package edu.senacsp.health_management.dto.response.restriction;

import edu.senacsp.health_management.entity.enums.RestrictionSeverity;
import edu.senacsp.health_management.entity.enums.RestrictionType;

import java.time.LocalDateTime;

/**
 * Response payload representing a Restriction.
 *
 * <p>NEVER used alone — always paired with a response wrapper.
 *
 * <p>Example JSON (wrapped):
 * <pre>{@code
 * {
 *     "data": {
 *         "id": 1,
 *         "profileId": 1,
 *         "type": "MEDICATION",
 *         "severity": "SEVERY",
 *         "title": "ALERGIA A DEXAMETASONA",
 *         "note": "Não tomar em hipótese nenhuma. Em caso de emergência ir ao pronto-socorro o mais rápido possível.",
 *         "active": true,
 *         "modifiedAt": "2026-09-27T15:55:02.802931",
 *         "createdAt": "2026-09-27T15:55:02.802931"
 *     },
 *     "message": "Successfully created a new restriction"
 * }
 * }</pre>
 */

public record CreateRestrictionResponse(
        Long id,
        Long profileId,
        RestrictionType type,
        RestrictionSeverity severity,
        String title,
        String note, // Optional
        boolean active,
        LocalDateTime modifiedAt,
        LocalDateTime createdAt
) {
    public CreateRestrictionResponse{
        if (note == null || note.isBlank())
        {
            note = "";
        }
    }
}