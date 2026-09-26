package edu.senacsp.health_management.dto.request.user;

public record SignupRequest(
        String name,
        String email,
        String password
) {}