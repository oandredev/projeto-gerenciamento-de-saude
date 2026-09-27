package edu.senacsp.health_management.dto.response.user;

public record UpdateUserResponse(
        Long id,
        String email,
        String name,
        boolean active
) {}
