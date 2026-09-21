package edu.senacsp.health_management.dto.request;

public record SignupRequest(
        String name,
        String email,
        String password
){}
