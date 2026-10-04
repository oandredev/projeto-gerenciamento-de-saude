package edu.senacsp.health_management.dto.response.user;

/* Used internally only by the service and controller layers; never sent to the client. */
public record AuthResponse(
        LoginResponse loginResponse,
        String token
) {}