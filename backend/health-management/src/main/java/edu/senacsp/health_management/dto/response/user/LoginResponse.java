package edu.senacsp.health_management.dto.response.user;

public record LoginResponse(
        String email,
        String username
) {}