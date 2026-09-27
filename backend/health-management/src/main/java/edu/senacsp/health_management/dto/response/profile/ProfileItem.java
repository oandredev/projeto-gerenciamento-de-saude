package edu.senacsp.health_management.dto.response.profile;

import edu.senacsp.health_management.entity.Profile;

/*
 * NEVER used alone — always paired with a response wrapper.
 */
public record ProfileItem(
        Long id,
        String name,
        Long userId,
        Long avatarId,
        boolean active
) {
    public ProfileItem (Profile profile)
    {
        this(
                profile.getId(),
                profile.getName(),
                profile.getUser().getId(),
                profile.getAvatarId(),
                profile.isActive()
        );
    }
}