import { useCallback, useState } from 'react';
import {
    ActivityIndicator,
    Pressable,
    ScrollView,
    StyleSheet,
    Text,
    View,
} from 'react-native';
import { MaterialCommunityIcons, Ionicons } from '@expo/vector-icons';
import { useFocusEffect } from '@react-navigation/native';
import Header from '../components/Header';
import { colors } from '../styles/theme';
import { getActiveProfile } from '../services/profileService';
import { listOccurrences, confirmOccurrence } from '../services/occurrenceService';
import { formatTime, getGreeting } from '../utils/format';// eu corrijo o time UTC e o horário para cumprimentar a saudação correta (bom dia, boa tarde, boa noite) de acordo com o horário local do usuário

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

function isToday(iso) {
    const d = new Date(iso);
    const now = new Date();
    return (
        d.getFullYear() === now.getFullYear() &&
        d.getMonth() === now.getMonth() &&
        d.getDate() === now.getDate()
    );
}

export default function HomeScreen() {
    const profile = getActiveProfile();
    const firstName = profile?.name?.split(' ')[0];
    const greeting = getGreeting();

    const [occurrences, setOccurrences] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    async function loadOccurrences() {
        if (!profile) {
            setLoading(false);
            return;
        }
        setLoading(true);
        setError('');
        try {
            const list = await listOccurrences(profile.id, { order: 'ASC' });
            setOccurrences(list.filter((o) => isToday(o.scheduledAt)));
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }

    // Recarrega sempre que a tela aparece
    useFocusEffect(
        useCallback(() => {
            loadOccurrences();
        }, [])
    );

    const nowItem = occurrences.find(
        (o) => o.type === 'MEDICATION' && o.status === 'PENDING'
    );
    const todayItems = occurrences.filter((o) => o !== nowItem);

    async function handleTaken() {
        try {
            await confirmOccurrence({
                scheduleId: nowItem.scheduleId,
                scheduledAt: nowItem.scheduledAt,
            });
            await loadOccurrences();
        } catch (e) {
            setError(e.message);
        }
    }

    return (
        <View style={styles.container}>
            <Header title={firstName ? `${greeting}, ${firstName}!` : `${greeting}!`} />

            <ScrollView contentContainerStyle={styles.content}>
                {loading && <ActivityIndicator size="large" color={colors.blue} />}

                {error !== '' && <Text style={styles.error}>{error}</Text>}

                {!loading && !error && occurrences.length === 0 && (
                    <Text style={styles.message}>
                        Nenhum remédio ou consulta para hoje.
                    </Text>
                )}

                {nowItem && (
                    <>
                        <Text style={styles.nowLabel}>Agora</Text>
                        <View style={styles.nowCard}>
                            <View style={styles.nowRow}>
                                <View style={styles.pillBox}>
                                    <MaterialCommunityIcons
                                        name="pill"
                                        size={28}
                                        color={colors.blue}
                                    />
                                </View>
                                <View style={styles.nowTexts}>
                                    <Text style={styles.nowName}>{nowItem.title}</Text>
                                    <Text style={styles.nowDetail}>
                                        Às {formatTime(nowItem.scheduledAt)}
                                    </Text>
                                </View>
                            </View>
                            <Pressable style={styles.takenButton} onPress={handleTaken}>
                                <Text style={styles.takenText}>Já tomei</Text>
                                <Ionicons name="checkmark" size={22} color={colors.white} />
                            </Pressable>
                        </View>
                    </>
                )}

                {todayItems.length > 0 && <Text style={styles.todayLabel}>Hoje</Text>}
                {todayItems.map((item) => (
                    <View
                        key={`${item.scheduleId}-${item.scheduledAt}`}
                        style={styles.item}
                    >
                        <View style={styles.itemTop}>
                            <Text style={styles.itemTime}>{formatTime(item.scheduledAt)}</Text>
                            <StatusChip occurrence={item} />
                        </View>
                        <Text style={styles.itemName}>{item.title}</Text>
                    </View>
                ))}
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
    nowLabel: {
        color: colors.orangeText,
        fontSize: 20,
        fontWeight: '600',
        marginBottom: 10,
    },
    nowCard: {
        borderWidth: 2,
        borderColor: colors.blue,
        borderRadius: 16,
        padding: 16,
        marginBottom: 28,
    },
    nowRow: {
        flexDirection: 'row',
        alignItems: 'center'
    },
    pillBox: {
        width: 56,
        height: 56,
        borderRadius: 14,
        backgroundColor: colors.blueBg,
        alignItems: 'center',
        justifyContent: 'center',
    },
    nowTexts: {
        flex: 1,
        marginLeft: 14
    },
    nowName: {
        color: colors.ink,
        fontSize: 22,
        fontWeight: '600'
    },
    nowDetail: {
        color: colors.gray,
        fontSize: 18,
        marginTop: 2
    },
    takenButton: {
        height: 56,
        borderRadius: 28,
        backgroundColor: colors.blue,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 8,
        marginTop: 16,
    },
    takenText: {
        color: colors.white,
        fontSize: 20,
        fontWeight: '600'
    },
    todayLabel: {
        color: colors.blue,
        fontSize: 20,
        fontWeight: '600',
        marginBottom: 10,
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
    itemName: {
        color: colors.ink,
        fontSize: 22,
        marginTop: 8
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