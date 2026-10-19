package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.Confirmation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {

    boolean existsBySchedule_IdAndScheduledAt(Long scheduleId, Instant scheduledAt);

    @Query("""
        SELECT c.schedule.id
        FROM Confirmation c
        WHERE c.id = :id
          AND c.schedule.profile.user.id = :userId
        """)
    Optional<Long> findScheduleIdOwnedBy(@Param("id") Long id, @Param("userId") Long userId);
}