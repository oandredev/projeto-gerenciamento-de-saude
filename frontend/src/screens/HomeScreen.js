import { Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { MaterialCommunityIcons, Ionicons } from '@expo/vector-icons';
import Header from '../components/Header';
import { colors } from '../styles/theme';

const NOW_ITEM = {
    name: 'Omeprazol 20 mg',
    detail: '14:00 - 1 comprimido',
};

const TODAY_ITEMS = [
    { id: '1', time: '08:00', name: 'Mebendazol 20 mg', status: 'taken' },
    { id: '2', time: '09:30', name: 'Losartana 50 mg', status: 'taken' },
    { id: '3', time: '12:00', name: 'Almoço - Insulina', status: 'taken' },
    { id: '4', time: '16:30', name: 'Dr. Hanschucrutz - UBS Santana', status: 'pending' },
    { id: '5', time: '18:00', name: 'Sinvastatina 20 mg', status: 'pending' },
    { id: '6', time: '20:00', name: 'Metformina 850 mg', status: 'pending' },
    { id: '7', time: '22:00', name: 'Vitamina D', status: 'pending' },
];

function StatusChip({ status }) {
    const taken = status === 'taken';
    const color = taken ? colors.blue : colors.orangeText;
    return (
        <View style={[styles.chip, taken ? styles.chipTaken : styles.chipPending]}>
            <Ionicons
                name={taken ? 'checkmark-circle' : 'time-outline'}
                size={20}
                color={color}
            />
            <Text style={[styles.chipText, { color }]}>
                {taken ? 'Tomado' : 'Pendente'}
            </Text>
        </View>
    );
}

export default function HomeScreen() {
    function handleTaken() {
        console.log('Já tomei:', NOW_ITEM.name);
    }

    return (
        <View style={styles.container}>
            <Header title="Boa tarde, Fernanda!" />

            <ScrollView contentContainerStyle={styles.content}>
                <Text style={styles.nowLabel}>Agora</Text>
                <View style={styles.nowCard}>
                    <View style={styles.nowRow}>
                        <View style={styles.pillBox}>
                            <MaterialCommunityIcons name="pill" size={28} color={colors.blue} />
                        </View>
                        <View style={styles.nowTexts}>
                            <Text style={styles.nowName}>{NOW_ITEM.name}</Text>
                            <Text style={styles.nowDetail}>{NOW_ITEM.detail}</Text>
                        </View>
                    </View>
                    <Pressable style={styles.takenButton} onPress={handleTaken}>
                        <Text style={styles.takenText}>Já tomei</Text>
                        <Ionicons name="checkmark" size={22} color={colors.white} />
                    </Pressable>
                </View>

                <Text style={styles.todayLabel}>Hoje</Text>
                {TODAY_ITEMS.map((item) => (
                    <View key={item.id} style={styles.item}>
                        <View style={styles.itemTop}>
                            <Text style={styles.itemTime}>{item.time}</Text>
                            <StatusChip status={item.status} />
                        </View>
                        <Text style={styles.itemName}>{item.name}</Text>
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
    nowTexts: { flex: 1, marginLeft: 14 },
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
    chipTaken: { backgroundColor: colors.blueBg },
    chipPending: { backgroundColor: colors.orangeBg },
    chipText: { fontSize: 16, fontWeight: '600' },
    chipTextTaken: { color: colors.blue },
    chipTextPending: { color: colors.orangeText },
});