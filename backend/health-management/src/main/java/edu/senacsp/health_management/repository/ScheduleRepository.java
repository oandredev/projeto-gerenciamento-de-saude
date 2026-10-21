package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.Schedule;
import edu.senacsp.health_management.entity.MedicationSchedule;
import edu.senacsp.health_management.entity.AppointmentSchedule;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
       SELECT s
       FROM Schedule s
       WHERE s.id = :id
         AND s.profile.user.id = :userId
         AND s.active = true
       """)
    Optional<Schedule> findActiveOwnedForUpdate(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * Loads every schedule of a profile together with its complementary rows, in a single query.
     *
     * <p>Both complementary tables are joined with {@code LEFT JOIN} on the shared ID, so a
     * schedule is never dropped: a MEDICATION schedule comes with the medication row, a CONSULTATION
     * or EXAM schedule comes with the appointment row (if it has one), and the other types come
     * with neither. Each row of the result is an array of three positions: {@code [0]} the
     * {@link Schedule} (never {@code null}), {@code [1]} the {@link MedicationSchedule}
     * (or {@code null}) and {@code [2]} the {@link AppointmentSchedule} (or {@code null}).
     *
     * <p>This query does not check who owns the profile. The caller must have already confirmed
     * that the profile belongs to the authenticated user.
     *
     * @param profile the profile whose schedules are loaded
     * @return one array per schedule, in the order described above (empty list if none)
     */
    @Query("""
        SELECT schedule, medication, appointment
        FROM Schedule schedule
        LEFT JOIN MedicationSchedule medication ON medication.id = schedule.id
        LEFT JOIN AppointmentSchedule appointment ON appointment.id = schedule.id
        WHERE schedule.profile = :profile
        """)
    List<Object[]> findAllWithDetailsByProfile(@Param("profile") Profile profile);
}