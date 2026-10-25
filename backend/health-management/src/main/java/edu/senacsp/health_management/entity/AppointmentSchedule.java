package edu.senacsp.health_management.entity;

import jakarta.persistence.*;

/**
 * Appointment data that complements a {@link Schedule} of type {@code CONSULTATION} or
 * {@code EXAM}.
 *
 * <p>Shares its primary key with the schedule ({@code @MapsId}), like
 * {@link MedicationSchedule}. Whether it is a consultation or an exam is given by the
 * parent's {@code type}, so it is not repeated here.
 */
@Entity
public class AppointmentSchedule {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private String doctorName;

    private String location;

    protected AppointmentSchedule() {} // Required by Hibernate

    public AppointmentSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    //-------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}