package edu.senacsp.health_management.dto.response.user;

public record SignupResponse(
        Long id,
        String name,
        String email
) {}