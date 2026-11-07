package edu.senacsp.health_management.dto.request.user;

/**
 * Request payload for updating the authenticated user's name and email.
 *
 * <p>Sent to {@code PUT /user}. Both {@code name} and {@code email} are always required.
 * The current password is only needed when the email is being changed; otherwise it is ignored.
 * To change the password, use {@code PUT /user/password}.
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *   "name": "Nome Teste",
 *   "email": "emailModificado@gmail.com",
 *   "currentPassword": "TesteAa.1"
 * }
 * }</pre>
 *
 * @param name            the user's display name (required)
 * @param email           the user's email (required)
 * @param currentPassword the current raw password (compared against the stored BCrypt hash);
 *                        required only when the email is being changed
 */
public record UpdateUserRequest(
        String name,
        String email,
        String currentPassword
) {}