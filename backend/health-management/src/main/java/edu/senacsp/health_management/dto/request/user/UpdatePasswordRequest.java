package edu.senacsp.health_management.dto.request.user;

/**
 * Request payload for changing the authenticated user's password.
 *
 * <p>Sent to {@code PUT /user/password}. Both fields are required.
 * The current password is checked first; only then is the new password validated.
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *   "currentPassword": "TesteAa.1",
 *   "newPassword": "NovaSenha.1"
 * }
 * }</pre>
 *
 * @param currentPassword the current raw password (compared against the stored BCrypt hash);
 *                        only its presence is validated, not the password rules
 * @param newPassword     the new raw password (validated with the same rules as in signup
 *                        and stored as a BCrypt hash)
 */
public record UpdatePasswordRequest(
        String currentPassword,
        String newPassword
) {}
