package edu.senacsp.health_management.dto.request.user;

public record LoginRequest (
        String email,
        String password,
        Boolean remember
) {}