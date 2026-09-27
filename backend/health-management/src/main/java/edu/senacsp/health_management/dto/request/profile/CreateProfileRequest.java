package edu.senacsp.health_management.dto.request.profile;

public record CreateProfileRequest(
        Long userId,
        String name,
        Long avatarId
) {}