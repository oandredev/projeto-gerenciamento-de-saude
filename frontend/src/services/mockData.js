// Dados de exemplo, no mesmo formato que a API do back-end devolve.
// Quando a API estiver ligada, estes dados saem e entram os reais.

function todayAt(hour, minute = 0) {
  const d = new Date();
  d.setHours(hour, minute, 0, 0);
  return d.toISOString();
}

function daysFromNow(days, hour, minute = 0) {
  const d = new Date();
  d.setDate(d.getDate() + days);
  d.setHours(hour, minute, 0, 0);
  return d.toISOString();
}

// GET /occurrence
export const mockOccurrences = [
  {
    confirmationId: 1,
    scheduleId: 1,
    title: 'Mebendazol 20 mg',
    type: 'MEDICATION',
    scheduledAt: todayAt(8),
    status: 'CONFIRMED',
    confirmedAt: todayAt(8, 5),
  },
  {
    confirmationId: 2,
    scheduleId: 2,
    title: 'Losartana 50 mg',
    type: 'MEDICATION',
    scheduledAt: todayAt(9, 30),
    status: 'CONFIRMED',
    confirmedAt: todayAt(9, 32),
  },
  {
    confirmationId: 3,
    scheduleId: 3,
    title: 'Insulina',
    type: 'MEDICATION',
    scheduledAt: todayAt(12),
    status: 'CONFIRMED',
    confirmedAt: todayAt(12, 3),
  },
  {
    confirmationId: 4,
    scheduleId: 4,
    title: 'Omeprazol 20 mg',
    type: 'MEDICATION',
    scheduledAt: todayAt(14),
    status: 'PENDING',
    confirmedAt: null,
  },
  {
    confirmationId: 5,
    scheduleId: 5,
    title: 'Dr. Hanschucrutz - UBS Santana',
    type: 'CONSULTATION',
    scheduledAt: todayAt(16, 30),
    status: 'PENDING',
    confirmedAt: null,
  },
  {
    confirmationId: 6,
    scheduleId: 6,
    title: 'Sinvastatina 20 mg',
    type: 'MEDICATION',
    scheduledAt: todayAt(18),
    status: 'PENDING',
    confirmedAt: null,
  },
  {
    confirmationId: 7,
    scheduleId: 7,
    title: 'Metformina 850 mg',
    type: 'MEDICATION',
    scheduledAt: todayAt(20),
    status: 'PENDING',
    confirmedAt: null,
  },
  {
    confirmationId: 8,
    scheduleId: 8,
    title: 'Vitamina D',
    type: 'MEDICATION',
    scheduledAt: todayAt(22),
    status: 'SCHEDULE',
    confirmedAt: null,
  },
];

// GET /schedule?type=MEDICATION
export const mockMedications = [
  {
    id: 1,
    profileId: 1,
    type: 'MEDICATION',
    title: 'Omeprazol 20 mg',
    startAt: todayAt(8),
    intervalValue: 12,
    intervalUnit: 'HOUR',
    endAt: null,
    notes: null,
    reminderValue: 10,
    reminderUnit: 'MINUTE',
    notifyEnabled: true,
    alarmEnabled: true,
    active: true,
    details: {
      brand: 'EMS',
      medicineName: 'Omeprazol',
      activeIngredient: 'omeprazol',
      dosageAmount: 20,
      dosageUnit: 'MG',
      stripe: 'NONE',
      photoUrl: null,
    },
  },
  {
    id: 2,
    profileId: 1,
    type: 'MEDICATION',
    title: 'Metformina 850 mg',
    startAt: todayAt(20),
    intervalValue: 24,
    intervalUnit: 'HOUR',
    endAt: null,
    notes: null,
    reminderValue: 10,
    reminderUnit: 'MINUTE',
    notifyEnabled: true,
    alarmEnabled: false,
    active: true,
    details: {
      brand: 'Medley',
      medicineName: 'Metformina',
      activeIngredient: 'metformina',
      dosageAmount: 850,
      dosageUnit: 'MG',
      stripe: 'RED',
      photoUrl: null,
    },
  },
];

// GET /schedule?type=CONSULTATION
export const mockAppointments = [
  {
    id: 10,
    profileId: 1,
    type: 'CONSULTATION',
    title: 'Consulta de rotina',
    startAt: daysFromNow(4, 16, 30),
    intervalValue: 0,
    intervalUnit: 'UNIQUE',
    endAt: null,
    notes: null,
    reminderValue: 1,
    reminderUnit: 'DAY',
    notifyEnabled: true,
    alarmEnabled: false,
    active: true,
    details: { doctorName: 'Dr. Hanschucrutz', location: 'UBS Santana' },
  },
  {
    id: 11,
    profileId: 1,
    type: 'CONSULTATION',
    title: 'Retorno',
    startAt: daysFromNow(9, 9, 0),
    intervalValue: 0,
    intervalUnit: 'UNIQUE',
    endAt: null,
    notes: null,
    reminderValue: 1,
    reminderUnit: 'DAY',
    notifyEnabled: true,
    alarmEnabled: false,
    active: true,
    details: { doctorName: 'Dra. Marina Alves', location: 'Hospital São Luiz' },
  },
];