package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.Confirmation;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {

    boolean existsBySchedule_IdAndScheduledAt(Long scheduleId, Instant scheduledAt);
    boolean existsBySchedule_Id(Long scheduleId);

    /**
     * Finds the ID of the schedule of a confirmation, only if that schedule's profile belongs
     * to the given user. Archived schedules are not filtered here.
     *
     * @param id     the confirmation ID
     * @param userId the ID of the user who must own the profile
     * @return the schedule ID, or empty if the confirmation does not exist or is not the user's
     */
    @Query("""
        SELECT confirmation.schedule.id
        FROM Confirmation confirmation
        WHERE confirmation.id = :id
          AND confirmation.schedule.profile.user.id = :userId
        """)
    Optional<Long> findScheduleIdOwnedBy(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * Finds the confirmations of a profile whose confirmation time is inside {@code [start, end)}
     * (start inclusive, end exclusive), so adjacent windows never overlap.
     *
     * <p>There is no {@code ORDER BY} on purpose: Spring Data appends it from {@code sort}.
     * Ownership is not checked here: the caller must have already verified the profile.
     *
     * @param profileId the ID of the profile
     * @param start     the start of the window (inclusive)
     * @param end       the end of the window (exclusive)
     * @param sort      the ordering to apply (for example, {@code confirmedAt} and {@code id})
     * @return the profile's confirmations in the window, empty if there are none
     */
    @Query("""
    SELECT confirmation FROM Confirmation confirmation
    WHERE confirmation.schedule.profile.id = :profileId
      AND confirmation.confirmedAt >= :start
      AND confirmation.confirmedAt < :end
    """)
    List<Confirmation> findAllByProfileBetween(@Param("profileId") Long profileId, @Param("start") Instant start, @Param("end") Instant end, Sort sort);

    /**
     * Finds the confirmations of the given schedules whose occurrence is inside the window
     * {@code [from, to]} (both inclusive). Used to cross with the calculated occurrences.
     *
     * <p>Ownership is not checked here: the schedules must have come from a query that already
     * checked it. There is no {@code ORDER BY}.
     *
     * @param ids  the schedule IDs; must not be {@code null} or empty
     * @param from the start of the window (inclusive)
     * @param to   the end of the window (inclusive)
     * @return the matching confirmations, empty if there are none
     */
    @Query("""
    SELECT confirmation FROM Confirmation confirmation
    WHERE confirmation.schedule.id IN :ids
      AND confirmation.scheduledAt BETWEEN :from AND :to
    """)
    List<Confirmation> findInWindow(@Param("ids") Collection<Long> ids, @Param("from") Instant from, @Param("to") Instant to);
}