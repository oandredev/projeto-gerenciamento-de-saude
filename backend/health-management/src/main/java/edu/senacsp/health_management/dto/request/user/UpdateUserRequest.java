package edu.senacsp.health_management.dto.request.user;

/**
 * Request payload for user update OR soft delete.
 *
 * <p>Sent to {@code PUT /user}.
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *   "email": "emailModificado@gmail.com",
 *   "password": "TesteAa.1",
 *   "name": "Nome Teste",
 *   "active": true
 * }
 * }</pre>
 *
 * @param email    the user's email
 * @param password the raw password (compared against the stored BCrypt hash)
 * @param name     the user's display name
 * @param active   used to soft delete
 */
public record UpdateUserRequest(
        String email,
        String password,
        String name,
        Boolean active
) {}