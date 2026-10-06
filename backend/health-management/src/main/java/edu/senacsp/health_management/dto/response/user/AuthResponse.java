package edu.senacsp.health_management.dto.response.user;

/* Used internally only by the service and controller layers; never sent to the client. */
public record AuthResponse(
        UserResponse userResponse,
        String token
) {}