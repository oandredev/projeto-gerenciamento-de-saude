package edu.senacsp.health_management.dto.response.schedule;

import edu.senacsp.health_management.entity.MedicationSchedule;
import edu.senacsp.health_management.entity.enums.DosageUnit;
import edu.senacsp.health_management.entity.enums.MedicationStripe;

public record MedicationDto(
        String brand,
        String medicineName,
        String activeIngredient,
        Double dosageAmount,
        DosageUnit dosageUnit,
        MedicationStripe stripe,
        String photoUrl
) implements ExtraScheduleDetails {

    public MedicationDto(MedicationSchedule m) {
        this(m.getBrand(), m.getMedicineName(), m.getActiveIngredient(),
                m.getDosageAmount(), m.getDosageUnit(), m.getStripe(), m.getPhotoUrl());
    }
}