package edu.senacsp.health_management.dto.request.user;

/**
 * Request payload for user login.
 *
 * <p>Sent to {@code POST /user/login}.
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *   "email": "andre@gmail.com",
 *   "password": "Teste12345Aa.",
 *   "remember": true
 * }
 * }</pre>
 *
 * @param email    the user's email
 * @param password the raw password (compared against the stored BCrypt hash)
 * @param remember if {@code true}, issues a longer-lived token ("remember me");
 */
public record LoginRequest (
        String email,
        String password,
        Boolean remember
) {}