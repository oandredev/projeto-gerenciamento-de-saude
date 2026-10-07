package edu.senacsp.health_management.dto.request.restriction;

import edu.senacsp.health_management.entity.enums.RestrictionSeverity;
import edu.senacsp.health_management.entity.enums.RestrictionType;


/**
 * Request payload for updating a restriction (full replacement, like an HTTP PUT).
 *
 * <p>Sent to {@code PUT /restriction/{id}}. The restriction ID comes from the URL,
 * not from the body. The {@code profileId} must be the profile the restriction
 * currently belongs to (it is checked together with the owner); it does not move
 * the restriction to another profile.
 *
 * <p>Example: {@code PUT /restriction/3}
 * <pre>{@code
 * {
 *   "profileId": 1,
 *   "type": "OTHER",
 *   "severity": "MODERATE",
 *   "title": "Title 2",
 *   "note": "NOTE 2",
 *   "active": false
 * }
 * }</pre>
 *
 * <p>Example without note: {@code PUT /restriction/3}
 * <pre>{@code
 * {
 *   "profileId": 1,
 *   "type": "OTHER",
 *   "severity": "MODERATE",
 *   "title": "Title 2",
 *   "active": true
 * }
 * }</pre>
 *
 * <p>All fields are required except {@code note}. A missing, {@code null} or blank
 * {@code note} becomes {@code ""}, and the service stores it as {@code null}.
 *
 * @param profileId the ID of the profile the restriction belongs to (must be the user's)
 * @param type      the kind of restriction (must match a {@link RestrictionType} constant)
 * @param severity  how serious it is (must match a {@link RestrictionSeverity} constant)
 * @param title     the short description (required)
 * @param note      optional details
 * @param active    {@code false} archives the restriction (soft delete), {@code true} reactivates it
 */
public record UpdateRestrictionRequest(
        Long profileId,
        RestrictionType type,
        RestrictionSeverity severity,
        String title,
        String note,
        Boolean active
) {
    public UpdateRestrictionRequest {
        if (note == null || note.isBlank())
        {
            note = "";
        }
    }
}