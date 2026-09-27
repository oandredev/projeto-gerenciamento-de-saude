package edu.senacsp.health_management.dto.response.user;

public record SignupResponse(
        Long id,
        String email,
        String name
) {}