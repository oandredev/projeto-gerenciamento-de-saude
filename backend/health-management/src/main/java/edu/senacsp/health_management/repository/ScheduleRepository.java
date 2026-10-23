package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.Schedule;

import edu.senacsp.health_management.entity.enums.ScheduleType;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.Collection;
import java.util.List;
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
        SELECT schedule
        FROM Schedule schedule
        WHERE schedule.id = :id
          AND schedule.profile.user.id = :userId
          AND schedule.active = true
        """)
    Optional<Schedule> findActiveOwnedForUpdate(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * Finds the active schedules of a profile, optionally restricted to some types, together
     * with their type-specific rows, in a single query.
     *
     * <p>Each element of the result is an array {@code [Schedule, MedicationSchedule,
     * AppointmentSchedule]}. The last two come from LEFT JOINs, so they are {@code null} when
     * the schedule has no row in that complementary table (a schedule has at most one of them).
     * Archived schedules ({@code active = false}) are never returned.
     *
     * <p>The query has no {@code ORDER BY} on purpose: Spring Data appends it from {@code sort}.
     * A fixed {@code ORDER BY} here would take precedence over the one requested.
     *
     * <p>Ownership is not checked here: the caller must have already verified that the profile
     * belongs to the user.
     *
     * @param profile the profile whose schedules are listed
     * @param types   the types to include; must not be {@code null} or empty (to include every
     *                type, pass all of them)
     * @param sort    the ordering to apply (for example, {@code startAt} and {@code id})
     * @return the rows {@code [Schedule, MedicationSchedule or null, AppointmentSchedule or null]},
     *         empty if there are none
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
}