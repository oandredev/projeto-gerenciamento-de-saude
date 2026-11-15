import { Pressable, StyleSheet, Switch, Text, View } from 'react-native';
import { colors } from '../styles/theme';

// Valores do back-end: reminderValue (número) + reminderUnit (MINUTE, HOUR...).
// "Na hora" = sem antecedência (null nos dois).
const REMINDER_OPTIONS = [
  { value: null, unit: null, label: 'Na hora' },
  { value: 5, unit: 'MINUTE', label: '5 min antes' },
  { value: 10, unit: 'MINUTE', label: '10 min antes' },
  { value: 30, unit: 'MINUTE', label: '30 min antes' },
  { value: 1, unit: 'HOUR', label: '1 hora antes' },
];

const UNIT_NAMES = {
  MINUTE: ['minuto', 'minutos'],
  HOUR: ['hora', 'horas'],
  DAY: ['dia', 'dias'],
  WEEK: ['semana', 'semanas'],
};

// Texto de um lembrete que não está na lista (ex.: criado por outro meio)
function customLabel(value, unit) {
  const [one, many] = UNIT_NAMES[unit] ?? ['minuto', 'minutos'];
  return `${value} ${value === 1 ? one : many} antes`;
}

function isSame(option, value, unit) {
  return (option.value ?? null) === (value ?? null) && (option.unit ?? null) === (unit ?? null);
}

// Campos "Alarme" e "Avisar antes", usados nos formulários de remédio e consulta.
export default function AlarmFields({
  alarmEnabled,
  onAlarmChange,
  reminderValue,
  reminderUnit,
  onReminderChange,
}) {
  // Se o item já tinha um lembrete fora da lista, ele aparece como mais uma opção
  const options = REMINDER_OPTIONS.some((o) => isSame(o, reminderValue, reminderUnit))
    ? REMINDER_OPTIONS
    : [
        ...REMINDER_OPTIONS,
        {
          value: reminderValue,
          unit: reminderUnit,
          label: customLabel(reminderValue, reminderUnit),
        },
      ];

  return (
    <View>
      <View style={styles.switchRow}>
        <View style={styles.switchText}>
          <Text style={styles.label}>Alarme</Text>
          <Text style={styles.hint}>
            {alarmEnabled
              ? 'O celular toca e vibra na hora do aviso.'
              : 'O aviso chega em silêncio.'}
          </Text>
        </View>
        <Switch
          value={alarmEnabled}
          onValueChange={onAlarmChange}
          trackColor={{ false: colors.border, true: colors.blue }}
          thumbColor={colors.white}
        />
      </View>

      <Text style={styles.label}>Avisar antes</Text>
      <View style={styles.chipRow}>
        {options.map((o) => {
          const selected = isSame(o, reminderValue, reminderUnit);
          return (
            <Pressable
              key={o.label}
              style={[styles.chip, selected && styles.chipSelected]}
              onPress={() => onReminderChange(o.value, o.unit)}
            >
              <Text style={[styles.chipText, selected && styles.chipTextSelected]}>
                {o.label}
              </Text>
            </Pressable>
          );
        })}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  switchRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: 12,
    marginBottom: 20,
  },
  switchText: { flex: 1 },
  label: {
    color: colors.blue,
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 8,
  },
  hint: { color: colors.gray, fontSize: 15, marginTop: -4 },
  chipRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
    marginBottom: 20,
  },
  chip: {
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderRadius: 18,
    borderWidth: 1.5,
    borderColor: colors.border,
    backgroundColor: colors.white,
  },
  chipSelected: { backgroundColor: colors.blue, borderColor: colors.blue },
  chipText: { color: colors.blue, fontSize: 16, fontWeight: '600' },
  chipTextSelected: { color: colors.white },
});