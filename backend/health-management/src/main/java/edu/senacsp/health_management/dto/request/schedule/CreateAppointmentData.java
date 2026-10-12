package edu.senacsp.health_management.dto.request.schedule;

public record CreateAppointmentData(
        String doctorName,
        String location
) implements ScheduleDetails {}