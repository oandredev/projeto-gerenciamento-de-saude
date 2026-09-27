package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
