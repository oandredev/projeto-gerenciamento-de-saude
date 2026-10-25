import { Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { MaterialCommunityIcons, Ionicons } from '@expo/vector-icons';
import Header from '../components/Header';
import { colors } from '../styles/theme';
import { mockOccurrences } from '../services/mockData';
import { formatTime } from '../utils/format';

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

export default function HomeScreen() {
    const occurrences = [...mockOccurrences].sort(
        (a, b) => new Date(a.scheduledAt) - new Date(b.scheduledAt)
    );
    const nowItem = occurrences.find(
        (o) => o.type === 'MEDICATION' && o.status === 'PENDING'
    );
    const todayItems = occurrences.filter((o) => o !== nowItem);

    function handleTaken() {
        console.log('Já tomei, confirmationId:', nowItem.confirmationId);
    }

    return (
        <View style={styles.container}>
            <Header title="Boa tarde, Fernanda!" />

            <ScrollView contentContainerStyle={styles.content}>
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

                <Text style={styles.todayLabel}>Hoje</Text>
                {todayItems.map((item) => (
                    <View key={item.confirmationId} style={styles.item}>
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
});