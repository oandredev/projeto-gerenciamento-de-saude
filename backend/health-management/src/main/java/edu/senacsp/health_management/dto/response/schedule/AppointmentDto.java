package edu.senacsp.health_management.dto.response.schedule;

import edu.senacsp.health_management.entity.AppointmentSchedule;

public record AppointmentDto(
        String doctorName,
        String location
) implements ExtraScheduleDetails {

    public AppointmentDto(AppointmentSchedule a) {
        this(a.getDoctorName(), a.getLocation());
    }
}