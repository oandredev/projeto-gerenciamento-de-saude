package edu.senacsp.health_management.dto.response.user;

import java.time.LocalDateTime;

public record UpdateUserResponse(
        Long id,
        String email,
        String name,
        Boolean active,
        LocalDateTime modifiedAt,
        LocalDateTime createdAt
) {}