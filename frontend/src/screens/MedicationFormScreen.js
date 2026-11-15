import { useState } from 'react';
import {
  Alert,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import Header from '../components/Header';
import DateTimeField from '../components/DateTimeField';
import AlarmFields from '../components/AlarmFields';
import { colors } from '../styles/theme';
import { getActiveProfile } from '../services/profileService';
import {
  createSchedule,
  deleteSchedule,
  updateSchedule,
} from '../services/scheduleService';
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

// O back-end só confirma ocorrências dentro de uma janela de cerca de 12 horas.
// Por isso o início pode estar no passado, mas no máximo 11 horas atrás.
const MAX_PAST_HOURS = 11;
const HOUR_MS = 60 * 60 * 1000;

// Converte o intervalo do back-end para "a cada quantas horas".
// O formulário só entende horas (1 a 24); outros intervalos voltam vazios.
function toIntervalInput(s) {
  if (s.intervalUnit === 'HOUR') return String(s.intervalValue);
  if (s.intervalUnit === 'DAY' && s.intervalValue === 1) return '24';
  return '';
}

// Agora, sem segundos (o seletor só mexe em data, hora e minuto)
function nowWithoutSeconds() {
  const d = new Date();
  d.setSeconds(0, 0);
  return d;
}

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
  const [startAt, setStartAt] = useState(
    schedule ? new Date(schedule.startAt) : nowWithoutSeconds()
  );
  const [endAt, setEndAt] = useState(schedule?.endAt ? new Date(schedule.endAt) : null);
  const [interval, setInterval] = useState(schedule ? toIntervalInput(schedule) : '');
  const [stripe, setStripe] = useState(details?.stripe ?? 'NONE');
  const [alarmEnabled, setAlarmEnabled] = useState(schedule?.alarmEnabled ?? false);
  const [reminderValue, setReminderValue] = useState(schedule?.reminderValue ?? null);
  const [reminderUnit, setReminderUnit] = useState(schedule?.reminderUnit ?? null);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  // Escolha do "Avisar antes" (valor e unidade andam juntos)
  function handleReminderChange(value, unitName) {
    setReminderValue(value);
    setReminderUnit(unitName);
  }

  async function handleSave() {
    const profile = getActiveProfile();
    const cleanName = name.trim();
    const amount = Number(dose.replace(',', '.'));
    const hours = Number(interval);

    if (!cleanName || !dose || !interval) {
      setError('Preencha o nome, a dose e o intervalo.');
      return;
    }
    if (!Number.isFinite(amount) || amount <= 0) {
      setError('A dose precisa ser um número maior que zero.');
      return;
    }
    if (!Number.isInteger(hours) || hours < 1 || hours > 24) {
      setError('O intervalo deve ser de 1 a 24 horas.');
      return;
    }

    // Ao editar, só confere o limite se a pessoa mudou o início
    const startChanged =
      !isEditing || startAt.getTime() !== new Date(schedule.startAt).getTime();
    if (startChanged && startAt.getTime() < Date.now() - MAX_PAST_HOURS * HOUR_MS) {
      setError(`O início pode ser no máximo ${MAX_PAST_HOURS} horas no passado.`);
      return;
    }
    if (endAt && endAt.getTime() <= startAt.getTime()) {
      setError('A data de fim precisa ser depois do início.');
      return;
    }
    if (!profile) {
      setError('Escolha um perfil antes de cadastrar.');
      return;
    }

    const unitLabel = DOSAGE_UNITS.find((u) => u.value === unit).label;
    const doseText = String(amount).replace('.', ',');

    // Campos que servem tanto para criar quanto para editar
    // (o back-end recebe as datas em UTC)
    const fields = {
      type: 'MEDICATION',
      title: `${cleanName} ${doseText} ${unitLabel}`,
      startAt: startAt.toISOString(),
      endAt: endAt ? endAt.toISOString() : undefined,
      intervalValue: hours === 24 ? 1 : hours,
      intervalUnit: hours === 24 ? 'DAY' : 'HOUR',
      alarmEnabled,
      reminderValue: reminderValue ?? undefined,
      reminderUnit: reminderUnit ?? undefined,
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
          notifyEnabled: schedule.notifyEnabled ?? true,
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

  // Deleta o remédio (no back-end ele só é marcado como inativo)
  async function remove() {
    setError('');
    setSaving(true);
    try {
      await deleteSchedule(schedule.id);
      navigation.goBack();
    } catch (e) {
      setError(e.message);
      setSaving(false);
    }
  }

  function handleDelete() {
    Alert.alert(
      'Deletar remédio',
      'Ele deixará de aparecer na lista e na tela Hoje. As confirmações já feitas ficam guardadas no histórico. Deseja continuar?',
      [
        { text: 'Cancelar', style: 'cancel' },
        { text: 'Deletar', style: 'destructive', onPress: remove },
      ]
    );
  }

  return (
    // No Android o teclado empurra a tela com "padding".
    // No iPhone quem cuida disso é o próprio ScrollView (automaticallyAdjustKeyboardInsets).
    <KeyboardAvoidingView
      style={styles.container}
      behavior={Platform.OS === 'android' ? 'padding' : undefined}
    >
      <Header
        title={isEditing ? 'Editar remédio' : 'Novo remédio'}
        showProfileSwitch={false}
        onBack={() => navigation.goBack()}
      />

      <ScrollView
        contentContainerStyle={styles.content}
        keyboardShouldPersistTaps="handled"
        automaticallyAdjustKeyboardInsets
        keyboardDismissMode="on-drag"
      >
        {/* Aviso: só aparece ao editar */}
        {isEditing && (
          <View style={styles.notice}>
            <Ionicons
              name="information-circle-outline"
              size={24}
              color={colors.orangeText}
            />
            <Text style={styles.noticeText}>
              Editar não altera as confirmações já realizadas. Depois de salvar,
              elas ficam como estão e não podem ser mudadas.
            </Text>
          </View>
        )}

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

        <DateTimeField
          label="Início (data e horário do primeiro)"
          value={startAt}
          onChange={setStartAt}
        />

        <DateTimeField
          label="Fim (opcional)"
          value={endAt}
          onChange={setEndAt}
          optional
          emptyText="Adicionar data de fim"
          defaultValue={new Date(startAt.getTime() + 7 * 24 * HOUR_MS)}
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

        <AlarmFields
          alarmEnabled={alarmEnabled}
          onAlarmChange={setAlarmEnabled}
          reminderValue={reminderValue}
          reminderUnit={reminderUnit}
          onReminderChange={handleReminderChange}
        />

        {error !== '' && <Text style={styles.error}>{error}</Text>}

        <Pressable
          style={styles.saveButton}
          onPress={handleSave}
          disabled={saving}
        >
          <Text style={styles.saveText}>
            {saving ? 'Salvando...' : isEditing ? 'Salvar edição' : 'Salvar'}
          </Text>
          {!saving && (
            <Ionicons name="checkmark" size={24} color={colors.orangeLight} />
          )}
        </Pressable>

        {/* Só aparece ao editar */}
        {isEditing && (
          <Pressable
            style={styles.deleteButton}
            onPress={handleDelete}
            disabled={saving}
          >
            <Ionicons name="trash-outline" size={20} color={colors.orangeText} />
            <Text style={styles.deleteText}>Deletar remédio</Text>
          </Pressable>
        )}
      </ScrollView>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: colors.white },
  content: { padding: 24, paddingBottom: 40 },
  notice: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    gap: 10,
    backgroundColor: colors.orangeBg,
    borderRadius: 14,
    padding: 14,
    marginBottom: 20,
  },
  noticeText: { flex: 1, color: colors.ink, fontSize: 15, lineHeight: 21 },
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
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    marginTop: 8,
  },
  saveText: { color: colors.white, fontSize: 20, fontWeight: '600' },
  deleteButton: {
    height: 56,
    borderRadius: 28,
    borderWidth: 1.5,
    borderColor: colors.orangeText,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    marginTop: 12,
  },
  deleteText: { color: colors.orangeText, fontSize: 18, fontWeight: '600' },
});
