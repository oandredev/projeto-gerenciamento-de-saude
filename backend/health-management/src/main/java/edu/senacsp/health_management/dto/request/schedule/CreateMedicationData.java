package edu.senacsp.health_management.dto.request.schedule;

import edu.senacsp.health_management.entity.enums.DosageUnit;
import edu.senacsp.health_management.entity.enums.MedicationStripe;

public record CreateMedicationData(
        String brand,
        String medicineName,
        String activeIngredient,
        Double dosageAmount,
        DosageUnit dosageUnit,
        MedicationStripe stripe
) implements ScheduleDetails {}
