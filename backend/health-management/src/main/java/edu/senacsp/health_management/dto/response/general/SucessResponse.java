package edu.senacsp.health_management.dto.response.general;

public record SucessResponse<T>(
        String message, // Debug
        T data
) {}