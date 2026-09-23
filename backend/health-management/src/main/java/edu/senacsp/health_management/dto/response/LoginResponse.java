package edu.senacsp.health_management.dto.response;

public record LoginResponse(
        String email,
        String username
) {}