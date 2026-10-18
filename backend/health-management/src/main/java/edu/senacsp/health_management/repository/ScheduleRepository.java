package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.Schedule;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * Finds an active schedule by ID, only if its profile belongs to the given user,
     * and locks the row.
     *
     * @param id the schedule ID
     * @param userId the ID of the user who must own the schedule's profile
     * @return the active schedule with a matching owner, or empty if not found
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       SELECT s
       FROM Schedule s
       WHERE s.id = :id
         AND s.profile.user.id = :userId
         AND s.active = true
       """)
    Optional<Schedule> findActiveOwnedForUpdate(@Param("id") Long id, @Param("userId") Long userId);
}