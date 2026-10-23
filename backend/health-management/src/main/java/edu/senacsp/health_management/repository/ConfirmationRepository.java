package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.Confirmation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {

    boolean existsBySchedule_IdAndScheduledAt(Long scheduleId, Instant scheduledAt);

    @Query("""
        SELECT confirmation.schedule.id
        FROM Confirmation confirmation
        WHERE confirmation.id = :id
          AND confirmation.schedule.profile.user.id = :userId
        """)
    Optional<Long> findScheduleIdOwnedBy(@Param("id") Long id, @Param("userId") Long userId);


    @Query("""
        SELECT confirmation FROM Confirmation confirmation
        WHERE confirmation.schedule.profile.id = :profileId
        ORDER BY confirmation.scheduledAt DESC, confirmation.id DESC
        """)
    List<Confirmation> findAllByProfile(@Param("profileId") Long profileId);
}