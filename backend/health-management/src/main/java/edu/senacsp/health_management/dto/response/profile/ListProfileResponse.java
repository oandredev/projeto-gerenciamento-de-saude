package edu.senacsp.health_management.dto.response.profile;

import java.util.List;

public record ListProfileResponse(
        List<ProfileItem> profileItemList
) {}
