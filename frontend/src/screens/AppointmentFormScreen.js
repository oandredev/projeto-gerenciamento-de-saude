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

// Os valores (CONSULTATION, EXAM) são os do back-end.
const TYPES = [
    { value: 'CONSULTATION', label: 'Consulta' },
    { value: 'EXAM', label: 'Exame' },
];

// Exemplo que aparece no campo "Nome" conforme o tipo escolhido
const TITLE_EXAMPLES = {
    CONSULTATION: 'Ex.: Cardiologista',
    EXAM: 'Ex.: Endoscopia',
};

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

export default function AppointmentFormScreen({ navigation, route }) {
    // Se veio um item da lista, estamos editando
    const schedule = route.params?.schedule;
    const isEditing = !!schedule;

    const [type, setType] = useState(schedule?.type ?? 'CONSULTATION');
    const [title, setTitle] = useState(schedule?.title ?? '');
    const [doctor, setDoctor] = useState(schedule?.details?.doctorName ?? '');
    const [location, setLocation] = useState(schedule?.details?.location ?? '');
    const [startAt, setStartAt] = useState(
        schedule ? new Date(schedule.startAt) : nowWithoutSeconds()
    );
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

    // Confere os campos e, se a data já passou, pergunta antes de salvar
    function handleSave() {
        const profile = getActiveProfile();

        if (!title.trim()) {
            setError('Preencha o nome.');
            return;
        }
        if (!profile) {
            setError('Escolha um perfil antes de cadastrar.');
            return;
        }

        // Ao editar, só avisa se a pessoa mudou a data
        const startChanged =
            !isEditing || startAt.getTime() !== new Date(schedule.startAt).getTime();

        if (startChanged && startAt.getTime() < Date.now()) {
            Alert.alert(
                'Data no passado',
                'Esta data e horário já passaram. Deseja salvar mesmo assim?',
                [
                    { text: 'Corrigir', style: 'cancel' },
                    { text: 'Salvar mesmo assim', onPress: save },
                ]
            );
            return;
        }

        save();
    }

    // Envia para o back-end
    async function save() {
        const profile = getActiveProfile();

        // Campos que servem tanto para criar quanto para editar
        // (a data e o horário vêm do seletor; o back-end recebe em UTC)
        const fields = {
            type,
            title: title.trim(),
            startAt: startAt.toISOString(),
            intervalUnit: 'UNIQUE',
            alarmEnabled,
            reminderValue: reminderValue ?? undefined,
            reminderUnit: reminderUnit ?? undefined,
            details: {
                doctorName: doctor.trim() || undefined,
                location: location.trim() || undefined,
            },
        };

        setError('');
        setSaving(true);
        try {
            if (isEditing) {
                // O PUT substitui tudo: devolvemos o que o item já tinha (e o formulário não mostra)
                await updateSchedule({
                    ...fields,
                    id: schedule.id,
                    notes: schedule.notes ?? undefined,
                    endAt: schedule.endAt ?? undefined,
                    notifyEnabled: schedule.notifyEnabled ?? true,
                });
            } else {
                await createSchedule({
                    ...fields,
                    profileId: profile.id,
                    notifyEnabled: true,
                });
            }
            navigation.goBack();
        } catch (e) {
            setError(e.message);
        } finally {
            setSaving(false);
        }
    }

    const typeLabel = type === 'EXAM' ? 'exame' : 'consulta';
    const headerTitle = isEditing
        ? `Editar ${typeLabel}`
        : type === 'EXAM'
            ? 'Novo exame'
            : 'Nova consulta';

    // Deleta o item (no back-end ele só é marcado como inativo)
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
            `Deletar ${typeLabel}`,
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
                title={headerTitle}
                showProfileSwitch={false}
                onBack={() => navigation.goBack()}
            />

            <ScrollView
                contentContainerStyle={styles.content}
                keyboardShouldPersistTaps="handled"
                automaticallyAdjustKeyboardInsets
                keyboardDismissMode="on-drag"
            >
                {/* O tipo não pode ser trocado depois de criado (regra do back-end) */}
                {!isEditing && (
                    <>
                        <Text style={styles.label}>Tipo</Text>
                        <View style={styles.chipRow}>
                            {TYPES.map((t) => (
                                <Chip
                                    key={t.value}
                                    label={t.label}
                                    selected={type === t.value}
                                    onPress={() => setType(t.value)}
                                />
                            ))}
                        </View>
                    </>
                )}

                <Text style={styles.label}>Nome</Text>
                <TextInput
                    style={styles.input}
                    placeholder={TITLE_EXAMPLES[type]}
                    placeholderTextColor={colors.placeholder}
                    value={title}
                    onChangeText={setTitle}
                />

                <Text style={styles.label}>Médico (opcional)</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Ex.: Dra. Silva"
                    placeholderTextColor={colors.placeholder}
                    value={doctor}
                    onChangeText={setDoctor}
                />

                <Text style={styles.label}>Local (opcional)</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Ex.: Clínica Central"
                    placeholderTextColor={colors.placeholder}
                    value={location}
                    onChangeText={setLocation}
                />

                <DateTimeField
                    label="Data e horário"
                    value={startAt}
                    onChange={setStartAt}
                />

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
                        <Text style={styles.deleteText}>Deletar {typeLabel}</Text>
                    </Pressable>
                )}
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