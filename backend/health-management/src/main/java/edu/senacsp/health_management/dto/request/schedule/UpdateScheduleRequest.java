package edu.senacsp.health_management.dto.request.schedule;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.senacsp.health_management.entity.enums.IntervalUnit;
import edu.senacsp.health_management.entity.enums.ReminderUnit;
import edu.senacsp.health_management.entity.enums.ScheduleType;

import java.time.Instant;

public record UpdateScheduleRequest(
        Long id,
        ScheduleType type,
        String title,
        Instant startAt,
        Integer intervalValue,
        IntervalUnit intervalUnit,
        Instant endAt,
        String notes,
        Integer reminderValue,
        ReminderUnit reminderUnit,
        Boolean notifyEnabled,
        Boolean alarmEnabled,

        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
                property = "type",
                visible = true)
        @JsonSubTypes({
                @JsonSubTypes.Type(value = CreateMedicationData.class, name = "MEDICATION"),
                @JsonSubTypes.Type(value = CreateAppointmentData.class, name = "CONSULTATION"),
                @JsonSubTypes.Type(value = CreateAppointmentData.class, name = "EXAM")
        })
        ScheduleDetails details
) {}