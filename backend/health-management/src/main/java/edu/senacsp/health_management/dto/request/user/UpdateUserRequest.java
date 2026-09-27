package edu.senacsp.health_management.dto.request.user;

public record UpdateUserRequest(
        Long id,
        String email,
        String password,
        String name,
        boolean active
) {}