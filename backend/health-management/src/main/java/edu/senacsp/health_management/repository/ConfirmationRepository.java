package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {

    boolean existsBySchedule_IdAndScheduledAt(Long scheduleId, Instant scheduledAt);
}