package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.AppointmentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentScheduleRepository extends JpaRepository<AppointmentSchedule, Long> {
}
