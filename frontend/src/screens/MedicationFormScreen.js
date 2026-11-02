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
import { createSchedule } from '../services/scheduleService';
import { STRIPE_LABELS } from '../utils/format';

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

function Chip({ label, selected, onPress }) {
  return (
    <Pressable
      style={[styles.chip, selected && styles.chipSelected]}
      onPress={onPress}
    >
      <Text style={[styles.chipText, selected && styles.chipTextSelected]}>
        {label}
      </Text>
    </Pressable>
  );
}

export default function MedicationFormScreen({ navigation }) {
  const [name, setName] = useState('');
  const [ingredient, setIngredient] = useState('');
  const [dose, setDose] = useState('');
  const [unit, setUnit] = useState('MG');
  const [time, setTime] = useState('');
  const [interval, setInterval] = useState('');
  const [stripe, setStripe] = useState('NONE');
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

    // Primeiro horário: hoje, no horário do celular. O back-end recebe em UTC.
    const startAt = new Date();
    startAt.setHours(Number(timeMatch[1]), Number(timeMatch[2]), 0, 0);

    const unitLabel = DOSAGE_UNITS.find((u) => u.value === unit).label;
    const doseText = String(amount).replace('.', ',');

    setError('');
    setSaving(true);
    try {
      await createSchedule({
        profileId: profile.id,
        type: 'MEDICATION',
        title: `${cleanName} ${doseText} ${unitLabel}`,
        startAt: startAt.toISOString(),
        intervalValue: hours === 24 ? 1 : hours,
        intervalUnit: hours === 24 ? 'DAY' : 'HOUR',
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
        title="Novo remédio"
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
        />

        <Text style={styles.label}>Princípio ativo (opcional)</Text>
        <TextInput
          style={styles.input}
          placeholder="Ex.: dipirona sódica"
          placeholderTextColor={colors.placeholder}
          value={ingredient}
          onChangeText={setIngredient}
        />

        <Text style={styles.label}>Dose</Text>
        <TextInput
          style={styles.input}
          placeholder="Ex.: 500"
          placeholderTextColor={colors.placeholder}
          keyboardType="decimal-pad"
          value={dose}
          onChangeText={setDose}
        />
        <View style={styles.chipRow}>
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
        />

        <Text style={styles.label}>Repetir a cada quantas horas</Text>
        <TextInput
          style={styles.input}
          placeholder="Ex.: 8"
          placeholderTextColor={colors.placeholder}
          keyboardType="number-pad"
          value={interval}
          onChangeText={setInterval}
        />

        <Text style={styles.label}>Tarja</Text>
        <View style={styles.chipRow}>
          {STRIPES.map((s) => (
            <Chip
              key={s}
              label={STRIPE_LABELS[s]}
              selected={stripe === s}
              onPress={() => setStripe(s)}
            />
          ))}
        </View>

        {error !== '' && <Text style={styles.error}>{error}</Text>}

        <Pressable
          style={styles.saveButton}
          onPress={handleSave}
          disabled={saving}
        >
          <Text style={styles.saveText}>{saving ? 'Salvando...' : 'Salvar'}</Text>
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