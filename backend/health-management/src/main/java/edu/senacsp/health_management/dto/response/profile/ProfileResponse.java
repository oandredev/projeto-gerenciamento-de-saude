package edu.senacsp.health_management.dto.response.profile;

import edu.senacsp.health_management.entity.Profile;
import java.time.LocalDateTime;

/*
 * NEVER used alone — always paired with a response wrapper.
 */
public record ProfileResponse(
        Long id,
        String name,
        Long avatarId,
        Boolean active,
        LocalDateTime modifiedAt,
        LocalDateTime createdAt
) {
    public ProfileResponse (Profile profile)
    {
        this(
                profile.getId(),
                profile.getName(),
                profile.getAvatarId(),
                profile.isActive(),
                profile.getModifiedAt(),
                profile.getCreatedAt()
        );
    }
}