package edu.senacsp.health_management.dto.response.restriction;

import edu.senacsp.health_management.entity.enums.RestrictionSeverity;
import edu.senacsp.health_management.entity.enums.RestrictionType;

import java.time.LocalDateTime;

public record UpdateRestrictionResponse(
        Long id,
        Long profileId,
        RestrictionType type,
        RestrictionSeverity severity,
        String title,
        String note,
        boolean active,
        LocalDateTime modifiedAt,
        LocalDateTime createdAt
) {
    public UpdateRestrictionResponse{
        if (note == null || note.isBlank())
        {
            note = "";
        }
    }
}