package edu.senacsp.health_management.dto.response.user;

import java.time.LocalDateTime;

public record LoginResponse(
        Long id,
        String email,
        String name,
        LocalDateTime modifiedAt,
        LocalDateTime createdAt
) {}