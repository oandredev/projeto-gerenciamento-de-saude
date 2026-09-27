package edu.senacsp.health_management.dto.response.user;

public record LoginResponse(
        Long id,
        String email,
        String name
) {}