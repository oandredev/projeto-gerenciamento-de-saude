import { useCallback, useState } from 'react';
import {
    ActivityIndicator,
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
import {
    listUpcomingNotifications,
    syncNotifications,
} from '../services/notificationService';
import { formatTime, pad } from '../utils/format';

// "Hoje", "Amanhã" ou a data (dd/mm)
function getDayLabel(timestamp) {
    const d = new Date(timestamp);
    const today = new Date();
    const startOfDay = (date) =>
        new Date(date.getFullYear(), date.getMonth(), date.getDate()).getTime();
    const diffDays = Math.round((startOfDay(d) - startOfDay(today)) / 86400000);

    if (diffDays === 0) return 'Hoje';
    if (diffDays === 1) return 'Amanhã';
    return `${pad(d.getDate())}/${pad(d.getMonth() + 1)}`;
}

export default function UpcomingNotificationsScreen({ navigation }) {
    const [items, setItems] = useState([]);
    const [granted, setGranted] = useState(true);
    const [loading, setLoading] = useState(true);
    const [refreshing, setRefreshing] = useState(false);

    // Atualiza os avisos com o back-end e depois lê o que ficou agendado no celular
    async function load() {
        try {
            await syncNotifications();
            const result = await listUpcomingNotifications();
            setItems(result.items);
            setGranted(result.granted);
        } catch (e) {
            setItems([]);
        } finally {
            setLoading(false);
            setRefreshing(false);
        }
    }

    // Recarrega sempre que a tela aparece
    useFocusEffect(
        useCallback(() => {
            load();
        }, [])
    );

    function handleRefresh() {
        setRefreshing(true);
        load();
    }

    return (
        <View style={styles.container}>
            <Header
                title="Próximos avisos"
                showProfileSwitch={false}
                onBack={() => navigation.goBack()}
            />

            <ScrollView
                contentContainerStyle={styles.content}
                refreshControl={
                    <RefreshControl refreshing={refreshing} onRefresh={handleRefresh} />
                }
            >
                <Text style={styles.intro}>
                    Avisos agendados neste celular para as próximas 48 horas. Puxe a tela
                    para baixo para atualizar.
                </Text>

                {loading && <ActivityIndicator size="large" color={colors.blue} />}

                {!loading && !granted && (
                    <View style={styles.notice}>
                        <Ionicons
                            name="notifications-off-outline"
                            size={24}
                            color={colors.orangeText}
                        />
                        <Text style={styles.noticeText}>
                            As notificações estão desligadas. Nos Ajustes do celular, abra
                            as notificações deste app e permita os avisos.
                        </Text>
                    </View>
                )}

                {!loading && granted && items.length === 0 && (
                    <Text style={styles.message}>
                        Nenhum aviso agendado nas próximas 48 horas.
                    </Text>
                )}

                {items.map((item) => (
                    <View key={item.id} style={styles.item}>
                        <View style={styles.itemTop}>
                            <View>
                                <Text style={styles.itemTime}>{formatTime(item.triggerAt)}</Text>
                                <Text style={styles.itemDay}>{getDayLabel(item.triggerAt)}</Text>
                            </View>
                            <View
                                style={[
                                    styles.chip,
                                    item.alarm ? styles.chipAlarm : styles.chipSilent,
                                ]}
                            >
                                <Ionicons
                                    name={item.alarm ? 'volume-high-outline' : 'volume-mute-outline'}
                                    size={20}
                                    color={item.alarm ? colors.orangeText : colors.blue}
                                />
                                <Text
                                    style={[
                                        styles.chipText,
                                        { color: item.alarm ? colors.orangeText : colors.blue },
                                    ]}
                                >
                                    {item.alarm ? 'Com alarme' : 'Silencioso'}
                                </Text>
                            </View>
                        </View>
                        <Text style={styles.itemTitle}>{item.title}</Text>
                        <Text style={styles.itemBody}>{item.body}</Text>
                    </View>
                ))}
            </ScrollView>
        </View>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, backgroundColor: colors.white },
    content: { padding: 24, paddingBottom: 32 },
    intro: { color: colors.gray, fontSize: 16, lineHeight: 22, marginBottom: 20 },
    message: {
        color: colors.gray,
        fontSize: 18,
        textAlign: 'center',
        marginTop: 24,
    },
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
    itemTime: { color: colors.blue, fontSize: 22, fontWeight: '700' },
    itemDay: { color: colors.gray, fontSize: 15, marginTop: 2 },
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
    chipAlarm: { backgroundColor: colors.orangeBg },
    chipSilent: { backgroundColor: colors.blueBg },
    chipText: { fontSize: 16, fontWeight: '600' },
    itemTitle: { color: colors.gray, fontSize: 16, marginTop: 10 },
    itemBody: { color: colors.ink, fontSize: 20, marginTop: 2 },
});