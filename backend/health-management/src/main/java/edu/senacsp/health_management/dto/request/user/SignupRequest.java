package edu.senacsp.health_management.dto.request.user;

/**
 * Request payload for user signup.
 *
 * <p>Sent to {@code POST /user/signup}.
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *   "email": "andre@gmail.com",
 *   "password": "Teste12345Aa.",
 *   "name": "André Rodrigues"
 * }
 * }</pre>
 *
 * @param email    the user's email (must be unique, otherwise 409)
 * @param password the raw password (stored only as a BCrypt hash)
 * @param name     the user's display name
 */
public record SignupRequest(
        String email,
        String password,
        String name
) {}