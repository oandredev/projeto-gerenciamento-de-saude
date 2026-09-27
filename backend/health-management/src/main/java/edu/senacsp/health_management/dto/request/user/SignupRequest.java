package edu.senacsp.health_management.dto.request.user;

public record SignupRequest(
        String email,
        String password,
        String name
) {}