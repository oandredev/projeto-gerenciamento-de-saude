package edu.senacsp.health_management.dto.response.restriction;

import edu.senacsp.health_management.entity.Restriction;
import edu.senacsp.health_management.entity.enums.RestrictionSeverity;
import edu.senacsp.health_management.entity.enums.RestrictionType;

import java.time.LocalDateTime;

public record RestrictionResponse(
        Long id,
        Long profileId,
        RestrictionType type,
        RestrictionSeverity severity,
        String title,
        String note, // Optional
        boolean active,
        LocalDateTime modifiedAt,
        LocalDateTime createdAt
) {
    public RestrictionResponse{
        if (note == null || note.isBlank())
        {
            note = "";
        }
    }

    public RestrictionResponse(Restriction r) {
        this(r.getId(), r.getProfile().getId(), r.getType(), r.getSeverity(),
                r.getTitle(), r.getNote(), r.isActive(), r.getModifiedAt(), r.getCreatedAt());
    }
}