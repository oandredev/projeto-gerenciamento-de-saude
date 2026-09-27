package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.MedicationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationScheduleRepository extends JpaRepository<MedicationSchedule, Long> {
}
