package edu.senacsp.health_management.dto.response.user;

import edu.senacsp.health_management.dto.response.profile.ProfileResponse;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(
        Long id,
        String email,
        String name,
        List<ProfileResponse> profiles,
        Boolean active,
        LocalDateTime modifiedAt,
        LocalDateTime createdAt
) {}