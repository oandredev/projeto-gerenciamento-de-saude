package edu.senacsp.health_management.dto.response.user;

import edu.senacsp.health_management.dto.response.profile.ProfileResponse;

import java.time.Instant;
import java.util.List;

public record UserResponse(
        Long id,
        String email,
        String name,
        List<ProfileResponse> profiles,
        Boolean active,
        Instant modifiedAt,
        Instant createdAt
) {}