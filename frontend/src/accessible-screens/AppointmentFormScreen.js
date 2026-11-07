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
import { pad, formatTime } from '../utils/format';

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

// Converte a data UTC do back-end para DD/MM/AAAA no relógio do celular
function toDateInput(iso) {
    const d = new Date(iso);
    return `${pad(d.getDate())}/${pad(d.getMonth() + 1)}/${d.getFullYear()}`;
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

export default function AppointmentFormScreen({ navigation, route }) {
    // Se veio um item da lista, estamos editando
    const schedule = route.params?.schedule;
    const isEditing = !!schedule;

    const [type, setType] = useState(schedule?.type ?? 'CONSULTATION');
    const [title, setTitle] = useState(schedule?.title ?? '');
    const [doctor, setDoctor] = useState(schedule?.details?.doctorName ?? '');
    const [location, setLocation] = useState(schedule?.details?.location ?? '');
    const [date, setDate] = useState(schedule ? toDateInput(schedule.startAt) : '');
    const [time, setTime] = useState(schedule ? formatTime(schedule.startAt) : '');
    const [error, setError] = useState('');
    const [saving, setSaving] = useState(false);

    async function handleSave() {
        const profile = getActiveProfile();
        const cleanTitle = title.trim();
        const dateMatch = date.trim().match(/^(\d{1,2})\/(\d{1,2})\/(\d{4})$/);
        const timeMatch = time.trim().match(/^([01]?\d|2[0-3]):([0-5]\d)$/);

        if (!cleanTitle || !date || !time) {
            setError('Preencha o nome, a data e o horário.');
            return;
        }
        if (!dateMatch) {
            setError('Digite a data no formato 20/11/2026.');
            return;
        }
        if (!timeMatch) {
            setError('Digite o horário no formato 14:30.');
            return;
        }
        if (!profile) {
            setError('Escolha um perfil antes de cadastrar.');
            return;
        }

        const day = Number(dateMatch[1]);
        const month = Number(dateMatch[2]);
        const year = Number(dateMatch[3]);

        // Data e horário no relógio do celular. O back-end recebe em UTC.
        const startAt = new Date(year, month - 1, day, Number(timeMatch[1]), Number(timeMatch[2]), 0, 0);

        // Se o dia ou o mês não existem (ex.: 31/02), o JavaScript "corrige" sozinho: aqui a gente barra.
        if (startAt.getDate() !== day || startAt.getMonth() !== month - 1) {
            setError('Essa data não existe. Confira o dia e o mês.');
            return;
        }

        // Campos que servem tanto para criar quanto para editar
        const fields = {
            type,
            title: cleanTitle,
            startAt: startAt.toISOString(),
            intervalUnit: 'UNIQUE',
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
                    reminderValue: schedule.reminderValue ?? undefined,
                    reminderUnit: schedule.reminderUnit ?? undefined,
                });
            } else {
                await createSchedule({
                    ...fields,
                    profileId: profile.id,
                    notifyEnabled: true,
                    alarmEnabled: false,
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

    return (
        <KeyboardAvoidingView
            style={styles.container}
            behavior={Platform.OS === 'ios' ? 'padding' : undefined}
        >
            <Header
                title={headerTitle}
                showProfileSwitch={false}
                onBack={() => navigation.goBack()}
            />

            <ScrollView
                contentContainerStyle={styles.content}
                keyboardShouldPersistTaps="handled"
            >
                {/* O tipo não pode ser trocado depois de criado (regra do back-end) */}
                {!isEditing && (
                    <>
                        <Text style={styles.label}>Tipo</Text>
                        <View style={styles.chipRow} accessibilityRole="radiogroup">
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
                    accessible
                    accessibilityLabel={`Nome da ${typeLabel}`}
                />

                <Text style={styles.label}>Médico (opcional)</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Ex.: Dra. Silva"
                    placeholderTextColor={colors.placeholder}
                    value={doctor}
                    onChangeText={setDoctor}
                    accessible
                    accessibilityLabel="Nome do médico, opcional"
                />

                <Text style={styles.label}>Local (opcional)</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Ex.: Clínica Central"
                    placeholderTextColor={colors.placeholder}
                    value={location}
                    onChangeText={setLocation}
                    accessible
                    accessibilityLabel="Local, opcional"
                />

                <Text style={styles.label}>Data</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Ex.: 20/11/2026"
                    placeholderTextColor={colors.placeholder}
                    keyboardType="numbers-and-punctuation"
                    value={date}
                    onChangeText={setDate}
                    accessible
                    accessibilityLabel="Data"
                    accessibilityHint="Digite no formato dia, mês e ano. Exemplo: 20/11/2026"
                />

                <Text style={styles.label}>Horário</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Ex.: 14:30"
                    placeholderTextColor={colors.placeholder}
                    keyboardType="numbers-and-punctuation"
                    value={time}
                    onChangeText={setTime}
                    accessible
                    accessibilityLabel="Horário"
                    accessibilityHint="Digite no formato horas e minutos. Exemplo: 14:30"
                />

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
                    accessibilityLabel={
                        isEditing ? `Salvar edição da ${typeLabel}` : `Salvar nova ${typeLabel}`
                    }
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
