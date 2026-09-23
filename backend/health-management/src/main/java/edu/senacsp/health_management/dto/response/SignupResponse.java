package edu.senacsp.health_management.dto.response;

public record SignupResponse(
        Long id,
        String name,
        String email
) {}