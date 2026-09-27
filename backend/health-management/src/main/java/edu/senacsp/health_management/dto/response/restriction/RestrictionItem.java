package edu.senacsp.health_management.dto.response.restriction;

import edu.senacsp.health_management.entity.Restriction;
import edu.senacsp.health_management.entity.enums.RestrictionSeverity;
import edu.senacsp.health_management.entity.enums.RestrictionType;

import java.time.LocalDateTime;

/*
 * NEVER used alone — always paired with a response wrapper.
 */
public record RestrictionItem(
        Long id,
        RestrictionType type,
        RestrictionSeverity severity,
        String title,
        String note,
        boolean active,
        LocalDateTime modifiedAt,
        LocalDateTime createdAt
) {
    public RestrictionItem(Restriction restriction)
    {
        this(
                restriction.getId(),
                restriction.getType(),
                restriction.getSeverity(),
                restriction.getTitle(),
                restriction.getNote(),
                restriction.isActive(),
                restriction.getModifiedAt(),
                restriction.getCreatedAt()
        );
    }
}