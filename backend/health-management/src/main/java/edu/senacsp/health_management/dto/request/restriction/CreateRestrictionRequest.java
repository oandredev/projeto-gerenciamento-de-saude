package edu.senacsp.health_management.dto.request.restriction;

import edu.senacsp.health_management.entity.enums.RestrictionSeverity;
import edu.senacsp.health_management.entity.enums.RestrictionType;

public record CreateRestrictionRequest(
        Long profileId,
        RestrictionType type,
        RestrictionSeverity severity,
        String title,
        String note,
        boolean active
) {
    public CreateRestrictionRequest{
        if (title == null || title.isBlank())
        {
            title = "";
        }

        if (note == null || note.isBlank())
        {
            note = "";
        }
    }
}