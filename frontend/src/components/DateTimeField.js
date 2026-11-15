import { useState } from 'react';
import { Platform, Pressable, StyleSheet, Text, View } from 'react-native';
import DateTimePicker from '@react-native-community/datetimepicker';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../styles/theme';

function two(n) {
  return String(n).padStart(2, '0');
}

// Ex.: 15/11/2026 08:30
function formatDateTime(d) {
  return `${two(d.getDate())}/${two(d.getMonth() + 1)}/${d.getFullYear()} ${two(
    d.getHours()
  )}:${two(d.getMinutes())}`;
}

// Junta o que a pessoa escolheu (só a data ou só a hora) com o valor que já existia
function merge(previous, picked, mode) {
  const next = new Date(previous);
  if (mode === 'date') {
    next.setFullYear(picked.getFullYear(), picked.getMonth(), picked.getDate());
  } else {
    next.setHours(picked.getHours(), picked.getMinutes(), 0, 0);
  }
  return next;
}

// Campo de data e hora.
// - value: Date (ou null, quando o campo é opcional e está vazio)
// - onChange: recebe a nova Date (ou null, ao remover)
// - optional: mostra "adicionar" e "remover"
// - defaultValue: a Date usada quando a pessoa toca em "adicionar"
export default function DateTimeField({
  label,
  value,
  onChange,
  optional = false,
  emptyText = 'Adicionar',
  defaultValue = new Date(),
}) {
  // Só no Android: qual seletor está aberto ('date', 'time' ou nenhum)
  const [androidMode, setAndroidMode] = useState(null);

  function handleAndroidChange(event, picked) {
    if (event.type === 'dismissed' || !picked) {
      setAndroidMode(null);
      return;
    }
    onChange(merge(value, picked, androidMode));
    // Depois de escolher a data, abre o seletor de hora
    setAndroidMode(androidMode === 'date' ? 'time' : null);
  }

  function handleIosChange(mode) {
    return (event, picked) => {
      if (picked) onChange(merge(value, picked, mode));
    };
  }

  return (
    <View style={styles.wrapper}>
      <Text style={styles.label}>{label}</Text>

      {/* Campo opcional ainda vazio */}
      {optional && !value && (
        <Pressable style={styles.addButton} onPress={() => onChange(defaultValue)}>
          <Ionicons name="add-circle-outline" size={22} color={colors.blue} />
          <Text style={styles.addText}>{emptyText}</Text>
        </Pressable>
      )}

      {value && (
        <View style={styles.row}>
          {Platform.OS === 'ios' ? (
            <View style={styles.iosPickers}>
              <DateTimePicker
                value={value}
                mode="date"
                display="compact"
                locale="pt-BR"
                accentColor={colors.blue}
                onChange={handleIosChange('date')}
              />
              <DateTimePicker
                value={value}
                mode="time"
                display="compact"
                locale="pt-BR"
                accentColor={colors.blue}
                onChange={handleIosChange('time')}
              />
            </View>
          ) : (
            <Pressable style={styles.input} onPress={() => setAndroidMode('date')}>
              <Text style={styles.inputText}>{formatDateTime(value)}</Text>
              <Ionicons name="calendar-outline" size={22} color={colors.gray} />
            </Pressable>
          )}

          {optional && (
            <Pressable
              style={styles.removeButton}
              onPress={() => onChange(null)}
              hitSlop={8}
            >
              <Ionicons name="close-circle-outline" size={20} color={colors.orangeText} />
              <Text style={styles.removeText}>Remover</Text>
            </Pressable>
          )}
        </View>
      )}

      {Platform.OS === 'android' && androidMode && value && (
        <DateTimePicker
          value={value}
          mode={androidMode}
          is24Hour
          display="default"
          onChange={handleAndroidChange}
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  wrapper: { marginBottom: 20 },
  label: {
    color: colors.blue,
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 8,
  },
  row: { flexDirection: 'row', alignItems: 'center', gap: 12 },
  iosPickers: { flexDirection: 'row', alignItems: 'center', gap: 8, flex: 1 },
  input: {
    flex: 1,
    height: 56,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    borderWidth: 1.5,
    borderColor: colors.border,
    borderRadius: 14,
    paddingHorizontal: 16,
    backgroundColor: colors.white,
  },
  inputText: { fontSize: 18, color: colors.ink },
  addButton: {
    height: 56,
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    borderWidth: 1.5,
    borderColor: colors.border,
    borderRadius: 14,
    paddingHorizontal: 16,
    backgroundColor: colors.white,
  },
  addText: { color: colors.blue, fontSize: 18, fontWeight: '600' },
  removeButton: { flexDirection: 'row', alignItems: 'center', gap: 4 },
  removeText: { color: colors.orangeText, fontSize: 15, fontWeight: '600' },
});