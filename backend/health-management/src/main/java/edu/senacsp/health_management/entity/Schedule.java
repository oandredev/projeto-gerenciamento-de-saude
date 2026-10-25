package edu.senacsp.health_management.entity;

import edu.senacsp.health_management.entity.enums.IntervalUnit;
import edu.senacsp.health_management.entity.enums.ReminderUnit;
import edu.senacsp.health_management.entity.enums.ScheduleType;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * The rule of a scheduled event of a {@link Profile} (what happens and how often).
 *
 * <p>Holds what is common to every kind of event. Data specific to a {@code type} lives in a
 * complementary table whose primary key is also a foreign key to {@code schedule.id}
 * (for example {@link MedicationSchedule}). Occurrences are not stored: they are calculated
 * from this rule, and only confirmed ones are recorded in the history.
 *
 * <p>Schedules are never deleted: they are archived with {@code active = false}. The
 * {@code type} cannot be changed after creation. When a change alters the meaning of a rule
 * that already has confirmations, the service archives this schedule and creates a new,
 * independent one.
 */
@Entity
@Table(indexes = @Index(columnList = "profile_id, active"))
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private ScheduleType type;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private Instant startAt;

    @Column(nullable = false)
    private int intervalValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IntervalUnit intervalUnit;

    private Instant endAt; // If ALWAYS = null

    @Column(length = 1000)
    private String notes;

    private Integer reminderValue;

    @Enumerated(EnumType.STRING)
    private ReminderUnit reminderUnit;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean notifyEnabled = true;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean alarmEnabled = false;

    @OneToMany(mappedBy = "schedule", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Confirmation> confirmations = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant modifiedAt;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    protected Schedule() {} // Required by Hibernate

    public Schedule(Profile profile, ScheduleType type, String title, Instant startAt,
                    int intervalValue, IntervalUnit intervalUnit, Instant endAt) {
        this.profile = profile;
        this.type = type;
        this.title = title;
        this.startAt = startAt;
        this.intervalValue = intervalValue;
        this.intervalUnit = intervalUnit;
        this.endAt = endAt;
    }

    //-------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ScheduleType getType() {
        return type;
    }

    public void setType(ScheduleType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public int getIntervalValue() {
        return intervalValue;
    }

    public void setIntervalValue(int intervalValue) {
        this.intervalValue = intervalValue;
    }

    public IntervalUnit getIntervalUnit() {
        return intervalUnit;
    }

    public void setIntervalUnit(IntervalUnit intervalUnit) {
        this.intervalUnit = intervalUnit;
    }

    public Instant getEndAt() {
        return endAt;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getReminderValue() {
        return reminderValue;
    }

    public void setReminderValue(Integer reminderValue) {
        this.reminderValue = reminderValue;
    }

    public ReminderUnit getReminderUnit() {
        return reminderUnit;
    }

    public void setReminderUnit(ReminderUnit reminderUnit) {
        this.reminderUnit = reminderUnit;
    }

    public boolean isNotifyEnabled() {
        return notifyEnabled;
    }

    public void setNotifyEnabled(boolean notifyEnabled) {
        this.notifyEnabled = notifyEnabled;
    }

    public boolean isAlarmEnabled() {
        return alarmEnabled;
    }

    public void setAlarmEnabled(boolean alarmEnabled) {
        this.alarmEnabled = alarmEnabled;
    }

    public List<Confirmation> getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(List<Confirmation> confirmations) {
        this.confirmations = confirmations;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}