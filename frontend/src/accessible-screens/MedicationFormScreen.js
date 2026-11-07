import { useState } from 'react';
import {
  KeyboardAvoidingView,
  Platform,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import Header from '../components/Header';
import { colors } from '../styles/theme';
import { getActiveProfile } from '../services/profileService';
import { createSchedule, updateSchedule } from '../services/scheduleService';
import { STRIPE_LABELS, formatTime } from '../utils/format';

// Os valores (MG, ML...) são os do back-end; o texto é o que a pessoa vê.
const DOSAGE_UNITS = [
  { value: 'MG', label: 'mg' },
  { value: 'G', label: 'g' },
  { value: 'MCG', label: 'mcg' },
  { value: 'ML', label: 'ml' },
  { value: 'DROPS', label: 'gotas' },
  { value: 'CAPSULE', label: 'cápsulas' },
  { value: 'AMPOULE', label: 'ampolas' },
  { value: 'OTHER', label: 'outro' },
];

const STRIPES = ['NONE', 'YELLOW', 'RED', 'BLACK'];

// Converte o intervalo do back-end para "a cada quantas horas".
// O formulário só entende horas (1 a 24); outros intervalos voltam vazios.
function toIntervalInput(s) {
  if (s.intervalUnit === 'HOUR') return String(s.intervalValue);
  if (s.intervalUnit === 'DAY' && s.intervalValue === 1) return '24';
  return '';
}

function Chip({ label, selected, onPress }) {
  return (
    <Pressable
      style={[styles.chip, selected && styles.chipSelected]}
      onPress={onPress}
      accessible
      accessibilityRole="radio"
      accessibilityLabel={label}
      accessibilityState={{ checked: selected }}
    >
      <Text style={[styles.chipText, selected && styles.chipTextSelected]}>
        {label}
      </Text>
    </Pressable>
  );
}

export default function MedicationFormScreen({ navigation, route }) {
  // Se veio um item da lista, estamos editando
  const schedule = route.params?.schedule;
  const isEditing = !!schedule;
  const details = schedule?.details;

  const [name, setName] = useState(details?.medicineName ?? '');
  const [ingredient, setIngredient] = useState(details?.activeIngredient ?? '');
  const [dose, setDose] = useState(
    details?.dosageAmount != null ? String(details.dosageAmount).replace('.', ',') : ''
  );
  const [unit, setUnit] = useState(details?.dosageUnit ?? 'MG');
  const [time, setTime] = useState(schedule ? formatTime(schedule.startAt) : '');
  const [interval, setInterval] = useState(schedule ? toIntervalInput(schedule) : '');
  const [stripe, setStripe] = useState(details?.stripe ?? 'NONE');
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  async function handleSave() {
    const profile = getActiveProfile();
    const cleanName = name.trim();
    const amount = Number(dose.replace(',', '.'));
    const hours = Number(interval);
    const timeMatch = time.trim().match(/^([01]?\d|2[0-3]):([0-5]\d)$/);

    if (!cleanName || !dose || !time || !interval) {
      setError('Preencha o nome, a dose, o horário e o intervalo.');
      return;
    }
    if (!Number.isFinite(amount) || amount <= 0) {
      setError('A dose precisa ser um número maior que zero.');
      return;
    }
    if (!timeMatch) {
      setError('Digite o horário no formato 08:00.');
      return;
    }
    if (!Number.isInteger(hours) || hours < 1 || hours > 24) {
      setError('O intervalo deve ser de 1 a 24 horas.');
      return;
    }
    if (!profile) {
      setError('Escolha um perfil antes de cadastrar.');
      return;
    }

    // Primeiro horário no relógio do celular (o back-end recebe em UTC).
    // Ao criar: hoje. Ao editar: mantém o dia original e troca só a hora.
    const startAt = isEditing ? new Date(schedule.startAt) : new Date();
    startAt.setHours(Number(timeMatch[1]), Number(timeMatch[2]), 0, 0);

    const unitLabel = DOSAGE_UNITS.find((u) => u.value === unit).label;
    const doseText = String(amount).replace('.', ',');

    // Campos que servem tanto para criar quanto para editar
    const fields = {
      type: 'MEDICATION',
      title: `${cleanName} ${doseText} ${unitLabel}`,
      startAt: startAt.toISOString(),
      intervalValue: hours === 24 ? 1 : hours,
      intervalUnit: hours === 24 ? 'DAY' : 'HOUR',
    };

    setError('');
    setSaving(true);
    try {
      if (isEditing) {
        // O PUT substitui tudo: devolvemos o que o remédio já tinha (e o formulário não mostra)
        await updateSchedule({
          ...fields,
          id: schedule.id,
          notes: schedule.notes ?? undefined,
          endAt: schedule.endAt ?? undefined,
          reminderValue: schedule.reminderValue ?? undefined,
          reminderUnit: schedule.reminderUnit ?? undefined,
          details: {
            medicineName: cleanName,
            activeIngredient: ingredient.trim() || undefined,
            dosageAmount: amount,
            dosageUnit: unit,
            stripe,
            brand: details?.brand ?? undefined,
            photoUrl: details?.photoUrl ?? undefined,
          },
        });
      } else {
        await createSchedule({
          ...fields,
          profileId: profile.id,
          notifyEnabled: true,
          alarmEnabled: false,
          details: {
            medicineName: cleanName,
            activeIngredient: ingredient.trim() || undefined,
            dosageAmount: amount,
            dosageUnit: unit,
            stripe,
          },
        });
      }
      navigation.goBack();
    } catch (e) {
      setError(e.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <Header
        title={isEditing ? 'Editar remédio' : 'Novo remédio'}
        showProfileSwitch={false}
        onBack={() => navigation.goBack()}
      />

      <ScrollView
        contentContainerStyle={styles.content}
        keyboardShouldPersistTaps="handled"
      >
        <Text style={styles.label}>Nome do remédio</Text>
        <TextInput
          style={styles.input}
          placeholder="Ex.: Dipirona"
          placeholderTextColor={colors.placeholder}
          value={name}
          onChangeText={setName}
          accessible
          accessibilityLabel="Nome do remédio"
        />

        <Text style={styles.label}>Princípio ativo (opcional)</Text>
        <TextInput
          style={styles.input}
          placeholder="Ex.: dipirona sódica"
          placeholderTextColor={colors.placeholder}
          value={ingredient}
          onChangeText={setIngredient}
          accessible
          accessibilityLabel="Princípio ativo, opcional"
        />

        <Text style={styles.label}>Dose</Text>
        <TextInput
          style={styles.input}
          placeholder="Ex.: 500"
          placeholderTextColor={colors.placeholder}
          keyboardType="decimal-pad"
          value={dose}
          onChangeText={setDose}
          accessible
          accessibilityLabel="Dose"
          accessibilityHint="Digite apenas o número. Escolha a unidade abaixo"
        />
        <View
          style={styles.chipRow}
          accessibilityRole="radiogroup"
          accessibilityLabel="Unidade da dose"
        >
          {DOSAGE_UNITS.map((u) => (
            <Chip
              key={u.value}
              label={u.label}
              selected={unit === u.value}
              onPress={() => setUnit(u.value)}
            />
          ))}
        </View>

        <Text style={styles.label}>Horário do primeiro</Text>
        <TextInput
          style={styles.input}
          placeholder="Ex.: 08:00"
          placeholderTextColor={colors.placeholder}
          keyboardType="numbers-and-punctuation"
          value={time}
          onChangeText={setTime}
          accessible
          accessibilityLabel="Horário do primeiro remédio"
          accessibilityHint="Digite no formato horas e minutos. Exemplo: 08:00"
        />

        <Text style={styles.label}>Repetir a cada quantas horas</Text>
        <TextInput
          style={styles.input}
          placeholder="Ex.: 8"
          placeholderTextColor={colors.placeholder}
          keyboardType="number-pad"
          value={interval}
          onChangeText={setInterval}
          accessible
          accessibilityLabel="Repetir a cada quantas horas"
          accessibilityHint="Digite um número de 1 a 24"
        />

        <Text style={styles.label}>Tarja</Text>
        <View
          style={styles.chipRow}
          accessibilityRole="radiogroup"
          accessibilityLabel="Tarja do remédio"
        >
          {STRIPES.map((s) => (
            <Chip
              key={s}
              label={STRIPE_LABELS[s]}
              selected={stripe === s}
              onPress={() => setStripe(s)}
            />
          ))}
        </View>

        {error !== '' && (
          <Text
            style={styles.error}
            accessibilityRole="alert"
            accessibilityLiveRegion="polite"
          >
            {error}
          </Text>
        )}

        <Pressable
          style={styles.saveButton}
          onPress={handleSave}
          disabled={saving}
          accessible
          accessibilityRole="button"
          accessibilityLabel={isEditing ? 'Salvar edição do remédio' : 'Salvar novo remédio'}
          accessibilityState={{ disabled: saving, busy: saving }}
        >
          <Text style={styles.saveText}>
            {saving ? 'Salvando...' : isEditing ? 'Salvar edição' : 'Salvar'}
          </Text>
        </Pressable>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: colors.white },
  content: { padding: 24, paddingBottom: 40 },
  label: {
    color: colors.blue,
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 8,
  },
  input: {
    height: 56,
    borderWidth: 1.5,
    borderColor: colors.border,
    borderRadius: 14,
    paddingHorizontal: 16,
    fontSize: 18,
    color: colors.ink,
    backgroundColor: colors.white,
    marginBottom: 20,
  },
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
  error: {
    color: colors.orangeText,
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 12,
  },
  saveButton: {
    height: 56,
    borderRadius: 28,
    backgroundColor: colors.blue,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 8,
  },
  saveText: { color: colors.white, fontSize: 20, fontWeight: '600' },
});
