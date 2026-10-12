package edu.senacsp.health_management.dto.request.schedule;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.senacsp.health_management.entity.enums.IntervalUnit;
import edu.senacsp.health_management.entity.enums.ReminderUnit;
import edu.senacsp.health_management.entity.enums.ScheduleType;

import java.time.Instant;

/**
 * Request payload for creating a schedule.
 *
 * <p>Sent to {@code POST /schedule}. Times are UTC instants (ISO-8601 with {@code Z}).
 * The {@code details} block depends on {@code type}: medication data for MEDICATION, and
 * optional appointment data for CONSULTATION or EXAM. Other types take no {@code details}.
 *
 * <p>Example (medication):
 * <pre>{@code
 * {
 *   "profileId": 1,
 *   "type": "MEDICATION",
 *   "title": "Dipirona 500mg",
 *   "startAt": "2026-10-11T11:00:00Z",
 *   "intervalValue": 8,
 *   "intervalUnit": "HOUR",
 *   "endAt": "2026-10-18T11:00:00Z",
 *   "notes": "After meals",
 *   "reminderValue": 10,
 *   "reminderUnit": "MINUTE",
 *   "notifyEnabled": true,
 *   "alarmEnabled": true,
 *   "details": {
 *     "brand": "CIMED",
 *     "medicineName": "Dipirona",
 *     "activeIngredient": "dipirona sodica",
 *     "dosageAmount": 500,
 *     "dosageUnit": "MG",
 *     "stripe": "NONE"
 *   }
 * }
 * }</pre>
 *
 * <p>Example (consultation, one time only):
 * <pre>{@code
 * {
 *   "profileId": 1,
 *   "type": "CONSULTATION",
 *   "title": "Cardiologist",
 *   "startAt": "2026-10-20T17:30:00Z",
 *   "intervalUnit": "UNIQUE",
 *   "details": { "doctorName": "Dr. Silva", "location": "Clinic X" }
 * }
 * }</pre>
 *
 * @param profileId     the profile the schedule belongs to (must be the user's)
 * @param type          the kind of event (cannot be changed later)
 * @param title         the short title shown in lists (required)
 * @param startAt       the event time, or the first repetition (UTC, may be in the past)
 * @param intervalValue repetition amount; required unless {@code intervalUnit} is UNIQUE
 * @param intervalUnit  repetition unit; UNIQUE means it happens only once
 * @param endAt         planned end (UTC); {@code null} means it continues indefinitely
 * @param notes         optional notes
 * @param reminderValue how long before the event to remind; {@code null} means no reminder
 * @param reminderUnit  unit of {@code reminderValue}
 * @param notifyEnabled whether a notification is sent (default true)
 * @param alarmEnabled  whether a sound alarm plays (default false)
 * @param details       type-specific data; its shape is chosen by {@code type}
 */
public record CreateScheduleRequest(
        Long profileId,
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