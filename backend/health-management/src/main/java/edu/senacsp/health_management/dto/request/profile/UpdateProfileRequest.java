package edu.senacsp.health_management.dto.request.profile;

/**
 * Request payload for updating a new Profile
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *     "id" : 1,
 *     "name" : "André R Teste Alt",
 *     "avatarId" : 5,
 *     "active" : false
 * }
 * }</pre>
 */
public record UpdateProfileRequest(
        Long id,
        String name,
        Long avatarId,
        Boolean active
) {}