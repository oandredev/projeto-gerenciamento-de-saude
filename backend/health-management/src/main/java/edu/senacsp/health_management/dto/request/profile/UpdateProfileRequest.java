package edu.senacsp.health_management.dto.request.profile;

import edu.senacsp.health_management.dto.response.profile.ProfileItem;

public record UpdateProfileRequest(
        ProfileItem profileItem
) {}
