package edu.senacsp.health_management.dto.request.profile;

/**
 * Request payload for updating a profile (full replacement, like an HTTP PUT).
 *
 * <p>Sent to {@code PUT /profile/{id}}. The profile ID comes from the URL, not from the body.
 *
 * <p>Example: {@code PUT /profile/1}
 * <pre>{@code
 * {
 *   "name": "André R Teste Alt",
 *   "avatarId": 5,
 *   "active": false
 * }
 * }</pre>
 *
 * @param name     the new profile name (required)
 * @param avatarId the ID of the chosen avatar (required)
 * @param active   {@code false} archives the profile (soft delete), {@code true} reactivates it
 */
public record UpdateProfileRequest(
        String name,
        Long avatarId,
        Boolean active
) {}