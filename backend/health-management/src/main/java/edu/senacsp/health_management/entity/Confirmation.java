package edu.senacsp.health_management.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Confirmation of one occurrence of a {@link Schedule}. If the row exists, the occurrence
 * was fulfilled. It is created on confirm and deleted on undo, only inside the 24-hour
 * window of the occurrence; after that it is locked.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"schedule_id", "scheduled_at"}))
public class Confirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false, updatable = false)
    private Schedule schedule;

    @Column(name = "scheduled_at", nullable = false, updatable = false)
    private Instant scheduledAt;

    @Column(nullable = false, updatable = false)
    private Instant confirmedAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant modifiedAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Confirmation() {}

    public Confirmation(Schedule schedule, Instant scheduledAt) {
        this.schedule = schedule;
        this.scheduledAt = scheduledAt;
        this.confirmedAt = Instant.now();
    }

    //-------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Instant getScheduledAt() {
        return scheduledAt;
    }

    public Instant getConfirmedAt() {
        return confirmedAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}