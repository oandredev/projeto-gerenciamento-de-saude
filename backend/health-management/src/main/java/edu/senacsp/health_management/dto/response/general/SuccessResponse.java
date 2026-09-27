package edu.senacsp.health_management.dto.response.general;

public record SuccessResponse<T>(
        T data,
        String message
) {}