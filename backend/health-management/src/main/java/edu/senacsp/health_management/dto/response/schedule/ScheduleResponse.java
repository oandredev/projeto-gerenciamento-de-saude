package edu.senacsp.health_management.dto.response.schedule;

import edu.senacsp.health_management.entity.Schedule;

import edu.senacsp.health_management.entity.enums.IntervalUnit;
import edu.senacsp.health_management.entity.enums.ReminderUnit;
import edu.senacsp.health_management.entity.enums.ScheduleType;

import java.time.Instant;

/**
 * Response payload representing a Schedule, for every type.
 *
 * <p>{@code details} holds the type-specific data and its shape depends on {@code type}:
 * medication data for MEDICATION, appointment data for CONSULTATION or EXAM, and {@code null}
 * for the other types (or for an appointment that has no data).
 *
 * <p>Example JSON (medication):
 * <pre>{@code
 * {
 *   "id": 3,
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
 *   "active": true,
 *   "details": {
 *     "brand": "CIMED",
 *     "medicineName": "Dipirona",
 *     "activeIngredient": "dipirona sodica",
 *     "dosageAmount": 500.000,
 *     "dosageUnit": "MG",
 *     "stripe": "NONE",
 *     "photoUrl": null
 *   },
 *   "modifiedAt": "2026-10-11T10:15:30.123456",
 *   "createdAt": "2026-10-11T10:15:30.123456"
 * }
 * }</pre>
 */
public record ScheduleResponse(
        Long id,
        Long profileId,
        ScheduleType type,
        String title,
        Instant startAt,
        int intervalValue,
        IntervalUnit intervalUnit,
        Instant endAt,
        String notes,
        Integer reminderValue,
        ReminderUnit reminderUnit,
        boolean notifyEnabled,
        boolean alarmEnabled,
        boolean active,
        ExtraScheduleDetails details,
        Instant modifiedAt,
        Instant createdAt
) {

    public ScheduleResponse(Schedule s, ExtraScheduleDetails details) {
        this(s.getId(), s.getProfile().getId(), s.getType(), s.getTitle(), s.getStartAt(),
                s.getIntervalValue(), s.getIntervalUnit(), s.getEndAt(), s.getNotes(),
                s.getReminderValue(), s.getReminderUnit(), s.isNotifyEnabled(),
                s.isAlarmEnabled(), s.isActive(), details,
                s.getModifiedAt(), s.getCreatedAt());
    }
}