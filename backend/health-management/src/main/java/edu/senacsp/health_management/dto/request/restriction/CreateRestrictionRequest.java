package edu.senacsp.health_management.dto.request.restriction;

import edu.senacsp.health_management.entity.enums.RestrictionSeverity;
import edu.senacsp.health_management.entity.enums.RestrictionType;

/**
 * Request payload for creating a restriction.
 *
 * <p>Sent to {@code POST /restriction}.
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *   "profileId": 1,
 *   "type": "FOOD",
 *   "severity": "SEVERY",
 *   "title": "Title",
 *   "note": "Note test"
 * }
 * }</pre>
 *
 *   <p>Example JSON 2:
 * <pre>{@code
 * {
 *   "profileId": 1,
 *   "type": "FOOD",
 *   "severity": "SEVERY",
 *   "title": "Title 2"
 * }
 * }</pre>
 *
 * <p>A missing, {@code null} or blank {@code title} or {@code note} becomes {@code ""}.
 * The service then rejects an empty title (CODE 400) and stores an empty note as {@code null}.
 *
 * @param profileId the ID of the profile the restriction belongs to (must be the user's)
 * @param type      the kind of restriction (must match a {@link RestrictionType} constant)
 * @param severity  how serious it is (must match a {@link RestrictionSeverity} constant)
 * @param title     the short description (required)
 * @param note      optional details
 */
public record CreateRestrictionRequest(
        Long profileId,
        RestrictionType type,
        RestrictionSeverity severity,
        String title,
        String note
) {
    public CreateRestrictionRequest{
        if (title == null || title.isBlank())
        {
            title = "";
        }

        if (note == null || note.isBlank())
        {
            note = "";
        }
    }
}