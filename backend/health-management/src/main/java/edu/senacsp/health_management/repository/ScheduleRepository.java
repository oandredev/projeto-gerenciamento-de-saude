package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.Schedule;

import edu.senacsp.health_management.entity.enums.ScheduleType;

import org.springframework.data.domain.Sort;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.time.Instant;
import java.util.Optional;
import java.util.Collection;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * Finds an active schedule by ID, only if its profile belongs to the given user, and
     * locks the row.
     *
     * @param id     the schedule ID
     * @param userId the ID of the user who must own the schedule's profile
     * @return the active schedule with a matching owner, or empty if not found
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT schedule
        FROM Schedule schedule
        WHERE schedule.id = :id
          AND schedule.profile.user.id = :userId
          AND schedule.active = true
        """)
    Optional<Schedule> findActiveOwnedForUpdate(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * Finds the active schedules of a profile, optionally restricted to some types, together
     * with their type-specific rows, in a single query. Each element is
     * {@code [Schedule, MedicationSchedule or null, AppointmentSchedule or null]}.
     *
     * <p>There is no {@code ORDER BY} on purpose: Spring Data appends it from {@code sort}.
     * Ownership is not checked here; the caller must have already verified the profile.
     *
     * @param profile the profile whose schedules are listed
     * @param types   the types to include; must not be {@code null} or empty
     * @param sort    the ordering to apply (for example, {@code startAt} and {@code id})
     * @return the rows with the schedule and its details, empty if there are none
     */
    @Query("""
        SELECT schedule, medication, appointment FROM Schedule schedule
        LEFT JOIN MedicationSchedule medication ON medication.schedule = schedule
        LEFT JOIN AppointmentSchedule appointment ON appointment.schedule = schedule
        WHERE schedule.profile = :profile
          AND schedule.active = true
          AND schedule.type IN :types
        """)
    List<Object[]> findAllWithDetailsByProfile(@Param("profile") Profile profile, @Param("types") Collection<ScheduleType> types, Sort sort);

    /**
     * Finds the active schedules of a profile whose rule can have occurrences inside the window
     * {@code [from, to]}, only if the profile belongs to the given user.
     *
     * <p>Coarse filter: it does not calculate occurrences (that is done by {@code OccurrenceUtil}),
     * so a returned schedule may still have none in the window. The schedule end is exclusive.
     * A profile that does not exist and one from another user both give an empty list. There is
     * no {@code ORDER BY}.
     *
     * @param userId    the ID of the user who must own the profile
     * @param profileId the ID of the profile whose schedules are searched
     * @param type      the type to filter by, or {@code null} for every type
     * @param from      the start of the window (inclusive)
     * @param to        the end of the window (inclusive)
     * @return the matching active schedules, empty if there are none
     */
    @Query("""
    SELECT schedule FROM Schedule schedule
    WHERE schedule.profile.id = :profileId
      AND schedule.profile.user.id = :userId
      AND schedule.active = true
      AND (:type is null or schedule.type = :type)
      AND schedule.startAt <= :to
      AND (schedule.endAt is null or schedule.endAt > :from)
    """)
    List<Schedule> findActiveInWindow(@Param("userId") Long userId, @Param("profileId") Long profileId, @Param("type") ScheduleType type, @Param("from") Instant from, @Param("to") Instant to);
}