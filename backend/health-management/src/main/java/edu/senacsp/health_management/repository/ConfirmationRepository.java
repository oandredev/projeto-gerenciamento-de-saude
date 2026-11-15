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

    /**
     * Tells whether any version of the schedule family has a confirmation. Decides between
     * editing in place and archiving to create a new version.
     */
    boolean existsBySchedule_RootId(String rootId);

    /**
     * Tells whether the occurrence at {@code scheduledAt} was already confirmed in any version
     * of the schedule family.
     */
    boolean existsBySchedule_RootIdAndScheduledAt(String rootId, Instant scheduledAt);

    /**
     * Finds the confirmations whose occurrence is inside {@code [from, to)} and whose schedule
     * belongs to one of the given families ({@code rootId}). A confirmation made on an archived
     * version is found through the family of the active one.
     *
     * <p>Ownership is not checked here: the root IDs must have come from a query that already
     * checked it. There is no {@code ORDER BY}.
     *
     * @param rootIds the family IDs; must not be {@code null} or empty
     * @param from    the start of the window (inclusive)
     * @param to      the end of the window (exclusive)
     * @return the matching confirmations, with their schedule loaded, empty if there are none
     */
    @Query("""
            SELECT c FROM Confirmation c JOIN FETCH c.schedule s
            WHERE s.rootId IN :rootIds
              AND c.scheduledAt >= :from AND c.scheduledAt < :to
            """)
    List<Confirmation> findInWindowByRoot(@Param("rootIds") Collection<String> rootIds,
                                          @Param("from") Instant from,
                                          @Param("to") Instant to);

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
}
