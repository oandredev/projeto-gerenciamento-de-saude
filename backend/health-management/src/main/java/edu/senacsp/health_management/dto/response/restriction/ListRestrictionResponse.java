package edu.senacsp.health_management.dto.response.restriction;

import java.util.List;

public record ListRestrictionResponse(
        List<RestrictionItem> restrictionItemList
) {}
