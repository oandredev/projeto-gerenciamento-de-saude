import { useCallback, useRef, useState } from 'react';
import {
    ActivityIndicator,
    Alert,
    Pressable,
    RefreshControl,
    ScrollView,
    StyleSheet,
    Text,
    View,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useFocusEffect } from '@react-navigation/native';
import Header from '../components/Header';
import { colors } from '../styles/theme';
import { getActiveProfile } from '../services/profileService';
import {
    listOccurrences,
    confirmOccurrence,
    unconfirmOccurrence,
} from '../services/occurrenceService';
import { formatTime, getGreeting, pad } from '../utils/format';

const STATUS_STYLES = {
    CONFIRMED: { icon: 'checkmark-circle', color: colors.blue, bg: colors.blueBg },
    PENDING: { icon: 'time-outline', color: colors.orangeText, bg: colors.orangeBg },
    SCHEDULE: { icon: 'calendar-outline', color: colors.blue, bg: colors.blueBg },
};

function getStatusLabel(occurrence) {
    if (occurrence.status === 'PENDING') return 'Pendente';
    if (occurrence.status === 'SCHEDULE') return 'Agendado';
    return occurrence.type === 'MEDICATION' ? 'Tomado' : 'Realizado';
}

// Texto do botão de confirmar, conforme o tipo
function getConfirmLabel(occurrence) {
    return occurrence.type === 'MEDICATION' ? 'Já tomei' : 'Realizado';
}

// "Hoje", "Amanhã", "Ontem" ou a data (dd/mm)
function getDayLabel(iso) {
    const d = new Date(iso);
    const today = new Date();
    const startOfDay = (date) =>
        new Date(date.getFullYear(), date.getMonth(), date.getDate()).getTime();
    const diffDays = Math.round((startOfDay(d) - startOfDay(today)) / 86400000);

    if (diffDays === 0) return 'Hoje';
    if (diffDays === 1) return 'Amanhã';
    if (diffDays === -1) return 'Ontem';
    return `${pad(d.getDate())}/${pad(d.getMonth() + 1)}`;
}

function StatusChip({ occurrence }) {
    const style = STATUS_STYLES[occurrence.status];
    return (
        <View style={[styles.chip, { backgroundColor: style.bg }]}>
            <Ionicons name={style.icon} size={20} color={style.color} />
            <Text style={[styles.chipText, { color: style.color }]}>
                {getStatusLabel(occurrence)}
            </Text>
        </View>
    );
}

export default function HomeScreen() {
    const profile = getActiveProfile();
    const firstName = profile?.name?.split(' ')[0];
    const greeting = getGreeting();

    const [occurrences, setOccurrences] = useState([]);
    const [now, setNow] = useState(Date.now());
    const [loading, setLoading] = useState(true);
    const [refreshing, setRefreshing] = useState(false);
    const [error, setError] = useState('');
    const [busyKey, setBusyKey] = useState(null);

    const scrollRef = useRef(null);
    const scrolledToNow = useRef(false);

    // silent = true recarrega sem mostrar o "carregando" (usado depois de confirmar)
    async function loadOccurrences({ silent = false } = {}) {
        if (!profile) {
            setLoading(false);
            return;
        }
        if (!silent) {
            setLoading(true);
            scrolledToNow.current = false;
        }
        setError('');
        try {
            // O back-end entrega toda a janela: mostramos tudo, sem filtrar por dia
            const list = await listOccurrences(profile.id, { order: 'DESC' });
            setOccurrences(list);
            setNow(Date.now());
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
            setRefreshing(false);
        }
    }

    // Recarrega sempre que a tela aparece
    useFocusEffect(
        useCallback(() => {
            loadOccurrences();
        }, [])
    );

    function handleRefresh() {
        setRefreshing(true);
        loadOccurrences({ silent: true });
    }

    // Confirma ou desfaz um item. O botão do card muda conforme o status.
    async function toggleConfirmation(item) {
        const key = `${item.scheduleId}-${item.scheduledAt}`;
        if (busyKey) return;

        setBusyKey(key);
        try {
            if (item.status === 'CONFIRMED') {
                await unconfirmOccurrence(item.confirmationId);
            } else {
                await confirmOccurrence({
                    scheduleId: item.scheduleId,
                    scheduledAt: item.scheduledAt,
                });
            }
            await loadOccurrences({ silent: true });
        } catch (e) {
            // Alert aparece mesmo que a lista esteja rolada
            Alert.alert('Não foi possível concluir', e.message);
        } finally {
            setBusyKey(null);
        }
    }

    function handlePress(item) {
        if (item.status === 'CONFIRMED') {
            Alert.alert(
                'Desfazer confirmação',
                `Deseja desfazer a confirmação de "${item.title}"?`,
                [
                    { text: 'Cancelar', style: 'cancel' },
                    { text: 'Desfazer', onPress: () => toggleConfirmation(item) },
                ]
            );
            return;
        }
        toggleConfirmation(item);
    }

    // Tudo depois de "agora" é futuro; o resto é passado. Os dois do mais distante
    // para o mais próximo do "agora", como o back-end entrega (ordem decrescente).
    const byTimeDesc = [...occurrences].sort(
        (a, b) => new Date(b.scheduledAt) - new Date(a.scheduledAt)
    );
    const future = byTimeDesc.filter((o) => new Date(o.scheduledAt).getTime() > now);
    const past = byTimeDesc.filter((o) => new Date(o.scheduledAt).getTime() <= now);

    function renderItem(item) {
        const key = `${item.scheduleId}-${item.scheduledAt}`;
        const confirmed = item.status === 'CONFIRMED';
        const busy = busyKey === key;

        return (
            <View key={key} style={styles.item}>
                <View style={styles.itemTop}>
                    <View>
                        <Text style={styles.itemTime}>{formatTime(item.scheduledAt)}</Text>
                        <Text style={styles.itemDay}>{getDayLabel(item.scheduledAt)}</Text>
                    </View>
                    <StatusChip occurrence={item} />
                </View>
                <Text style={styles.itemName}>{item.title}</Text>

                <Pressable
                    style={[styles.actionButton, confirmed && styles.undoButton]}
                    onPress={() => handlePress(item)}
                    disabled={!!busyKey}
                >
                    {busy ? (
                        <ActivityIndicator
                            size="small"
                            color={confirmed ? colors.blue : colors.white}
                        />
                    ) : (
                        <>
                            <Text
                                style={[styles.actionText, confirmed && styles.undoText]}
                            >
                                {confirmed ? 'Desfazer' : getConfirmLabel(item)}
                            </Text>
                            <Ionicons
                                name={confirmed ? 'arrow-undo-outline' : 'checkmark'}
                                size={22}
                                color={confirmed ? colors.blue : colors.white}
                            />
                        </>
                    )}
                </Pressable>
            </View>
        );
    }

    // Linha que separa o futuro (em cima) do passado (embaixo)
    function renderNowDivider() {
        return (
            <View
                key="now-divider"
                style={styles.dividerRow}
                onLayout={(e) => {
                    // Na primeira vez, rola a tela até o "Agora"
                    if (!scrolledToNow.current) {
                        scrolledToNow.current = true;
                        scrollRef.current?.scrollTo({
                            y: Math.max(e.nativeEvent.layout.y - 90, 0),
                            animated: false,
                        });
                    }
                }}
            >
                <View style={styles.dividerLine} />
                <View style={styles.dividerPill}>
                    <Ionicons name="time-outline" size={18} color={colors.white} />
                    <Text style={styles.dividerText}>
                        Agora, {formatTime(new Date(now).toISOString())}
                    </Text>
                </View>
                <View style={styles.dividerLine} />
            </View>
        );
    }

    return (
        <View style={styles.container}>
            <Header title={firstName ? `${greeting}, ${firstName}!` : `${greeting}!`} />

            <ScrollView
                ref={scrollRef}
                contentContainerStyle={styles.content}
                refreshControl={
                    <RefreshControl refreshing={refreshing} onRefresh={handleRefresh} />
                }
            >
                {loading && <ActivityIndicator size="large" color={colors.blue} />}

                {error !== '' && <Text style={styles.error}>{error}</Text>}

                {!loading && !error && occurrences.length === 0 && (
                    <Text style={styles.message}>
                        Nenhum remédio ou consulta nesse período.
                    </Text>
                )}

                {!loading && occurrences.length > 0 && (
                    <>
                        {future.map(renderItem)}
                        {renderNowDivider()}
                        {past.map(renderItem)}
                    </>
                )}
            </ScrollView>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: colors.white
    },
    content: {
        padding: 24,
        paddingBottom: 32
    },
    dividerRow: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 10,
        marginVertical: 10,
        marginBottom: 24,
    },
    dividerLine: {
        flex: 1,
        height: 2,
        backgroundColor: colors.blue,
        borderRadius: 1,
    },
    dividerPill: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 6,
        backgroundColor: colors.blue,
        borderRadius: 16,
        paddingVertical: 6,
        paddingHorizontal: 14,
    },
    dividerText: {
        color: colors.white,
        fontSize: 16,
        fontWeight: '700'
    },
    item: {
        borderWidth: 1.5,
        borderColor: colors.border,
        borderRadius: 16,
        padding: 18,
        marginBottom: 14,
    },
    itemTop: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    itemTime: {
        color: colors.blue,
        fontSize: 22,
        fontWeight: '700'
    },
    itemDay: {
        color: colors.gray,
        fontSize: 15,
        marginTop: 2
    },
    itemName: {
        color: colors.ink,
        fontSize: 22,
        marginTop: 8
    },
    actionButton: {
        height: 52,
        borderRadius: 26,
        backgroundColor: colors.blue,
        borderWidth: 1.5,
        borderColor: colors.blue,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 8,
        marginTop: 14,
    },
    undoButton: {
        backgroundColor: colors.white,
    },
    actionText: {
        color: colors.white,
        fontSize: 18,
        fontWeight: '600'
    },
    undoText: {
        color: colors.blue
    },
    chip: {
        flexDirection: 'row',
        alignItems: 'center',
        alignSelf: 'flex-start',
        gap: 6,
        paddingVertical: 5,
        paddingLeft: 10,
        paddingRight: 14,
        borderRadius: 16,
    },
    chipText: {
        fontSize: 16,
        fontWeight: '600'
    },
    message: {
        color: colors.gray,
        fontSize: 18,
        textAlign: 'center',
        marginTop: 24,
    },
    error: {
        color: colors.orangeText,
        fontSize: 16,
        fontWeight: '600',
        marginBottom: 12,
    },
});