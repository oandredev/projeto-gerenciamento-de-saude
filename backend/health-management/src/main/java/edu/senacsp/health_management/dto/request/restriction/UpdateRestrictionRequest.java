package edu.senacsp.health_management.dto.request.restriction;

import edu.senacsp.health_management.entity.enums.RestrictionSeverity;
import edu.senacsp.health_management.entity.enums.RestrictionType;

public record UpdateRestrictionRequest(
        Long id,
        Long profileId,
        RestrictionType type,
        RestrictionSeverity severity,
        String title,
        String note,
        boolean active
) {
    public UpdateRestrictionRequest{
        if (note == null || note.isBlank())
        {
            note = "";
        }
    }
}