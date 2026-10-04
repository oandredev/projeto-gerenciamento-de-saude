package edu.senacsp.health_management.dto.request.profile;

/**
 * Request payload for creating a new Profile
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *     "name": "André R",
 *     "avatarId": 0
 * }
 * }</pre>
 */
public record CreateProfileRequest(
        String name,
        Long avatarId
) {}