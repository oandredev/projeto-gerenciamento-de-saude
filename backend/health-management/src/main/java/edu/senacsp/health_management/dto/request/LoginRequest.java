package edu.senacsp.health_management.dto.request;

public record LoginRequest (
    String email,
    String password,
    Boolean remember
){}
