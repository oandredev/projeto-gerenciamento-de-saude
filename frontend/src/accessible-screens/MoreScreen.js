import { useCallback, useState } from 'react';
import { Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import { useFocusEffect } from '@react-navigation/native';
import Header from '../components/Header';
import { colors } from '../styles/theme';
import { getActiveProfile, setActiveProfile } from '../services/profileService';
import { listSchedules } from '../services/scheduleService';
import { listRestrictions } from '../services/restrictionService';
import { setToken } from '../services/api';
import { getCurrentUser, clearCurrentUser } from '../services/authService';

const HIDDEN = {
    accessibilityElementsHidden: true,
    importantForAccessibility: 'no-hide-descendants',
};

function SummaryTile({ icon, value, label, onPress, hint }) {
    const spoken = value === '-' ? 'indisponível' : value;
    return (
        <Pressable
            style={styles.tile}
            onPress={onPress}
            accessible
            accessibilityRole="button"
            accessibilityLabel={`${label}: ${spoken}`}
            accessibilityHint={hint}
        >
            <MaterialCommunityIcons name={icon} size={26} color={colors.white} />
            <Text style={styles.tileValue}>{value}</Text>
            <Text style={styles.tileLabel}>{label}</Text>
        </Pressable>
    );
}

function MenuItem({ icon, label, subtitle, onPress, danger = false, hint }) {
    const color = danger ? colors.orangeText : colors.blue;
    return (
        <Pressable
            style={styles.item}
            onPress={onPress}
            accessible
            accessibilityRole="button"
            accessibilityLabel={subtitle ? `${label}, ${subtitle}` : label}
            accessibilityHint={hint}
        >
            <View style={[styles.itemIcon, danger && styles.itemIconDanger]}>
                <Ionicons name={icon} size={24} color={color} />
            </View>
            <View style={styles.itemTexts}>
                <Text style={[styles.itemLabel, { color }]}>{label}</Text>
                {subtitle ? <Text style={styles.itemSubtitle}>{subtitle}</Text> : null}
            </View>
            <Ionicons name="chevron-forward" size={22} color={colors.gray} />
        </Pressable>
    );
}

export default function MoreScreen({ navigation }) {
    const profile = getActiveProfile();

    // null = ainda não carregou (ou deu erro): os quadradinhos mostram "-"
    const [counts, setCounts] = useState(null);

    async function loadCounts() {
        if (!profile) return;
        try {
            const [schedules, restrictions] = await Promise.all([
                listSchedules(profile.id),
                listRestrictions(profile.id),
            ]);
            setCounts({
                medications: schedules.filter((s) => s.type === 'MEDICATION').length,
                appointments: schedules.filter(
                    (s) => s.type === 'CONSULTATION' || s.type === 'EXAM'
                ).length,
                // O back-end devolve também as arquivadas: contamos só as ativas
                restrictions: restrictions.filter((r) => r.active).length,
            });
        } catch (e) {
            setCounts(null);
        }
    }

    // Atualiza os números sempre que a tela aparece
    useFocusEffect(
        useCallback(() => {
            loadCounts();
        }, [])
    );

    const restrictionsSubtitle = counts
        ? counts.restrictions === 0
            ? 'Nenhuma cadastrada'
            : `${counts.restrictions} ${counts.restrictions === 1 ? 'ativa' : 'ativas'}`
        : undefined;

    // O back-end não tem rota de logout: sair é apagar o token e o perfil aqui no app.
    function handleLogout() {
        setToken(null);
        setActiveProfile(null);
        clearCurrentUser();
        navigation.getParent().reset({ index: 0, routes: [{ name: 'Login' }] });
    }

    const profileName = profile?.name ?? 'Nenhum perfil';

    return (
        <View style={styles.container}>
            <Header title="Mais" showProfileSwitch={false} logoInverted />

            <ScrollView contentContainerStyle={styles.content}>
                <View
                    style={styles.profileCard}
                    accessible
                    accessibilityLabel={`Acompanhando: ${profileName}`}
                >
                    <View style={styles.avatar} {...HIDDEN}>
                        <Text style={styles.avatarText}>
                            {profile?.name?.charAt(0).toUpperCase() ?? '?'}
                        </Text>
                    </View>
                    <View>
                        <Text style={styles.profileLabel}>Acompanhando</Text>
                        <Text style={styles.profileName}>{profileName}</Text>
                    </View>
                </View>

                <View style={styles.tilesRow}>
                    <SummaryTile
                        icon="pill"
                        value={counts ? counts.medications : '-'}
                        label="Remédios"
                        hint="Abre a lista de remédios"
                        onPress={() => navigation.navigate('Medications', { from: 'More' })}
                    />
                    <SummaryTile
                        icon="stethoscope"
                        value={counts ? counts.appointments : '-'}
                        label="Consultas"
                        hint="Abre a lista de consultas"
                        onPress={() => navigation.navigate('Appointments', { from: 'More' })}
                    />
                    <SummaryTile
                        icon="alert-circle-outline"
                        value={counts ? counts.restrictions : '-'}
                        label="Restrições"
                        hint="Abre a lista de restrições"
                        onPress={() => navigation.navigate('Restrictions')}
                    />
                </View>
                <MenuItem
                    icon="alert-circle-outline"
                    label="Restrições"
                    subtitle={restrictionsSubtitle}
                    onPress={() => navigation.navigate('Restrictions')}
                />
                <MenuItem
                    icon="person-outline"
                    label="Editar perfil"
                    onPress={() => navigation.navigate('ProfileEdit')}
                />
                <MenuItem
                    icon="swap-horizontal"
                    label="Trocar perfil"
                    onPress={() => navigation.navigate('ProfileSelect')}
                />
                <MenuItem
                    icon="person-circle-outline"
                    label="Minha conta"
                    subtitle={getCurrentUser()?.email}
                    onPress={() => navigation.navigate('AccountEdit')}
                />
                <MenuItem
                    icon="log-out-outline"
                    label="Sair"
                    hint="Encerra a sessão e volta para a tela de login"
                    onPress={handleLogout}
                    danger
                />
            </ScrollView>
        </View>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, backgroundColor: colors.blue },
    content: { padding: 24 },
    profileCard: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 16,
        backgroundColor: 'rgba(255,255,255,0.15)',
        borderRadius: 16,
        padding: 18,
        marginBottom: 14,
    },
    avatar: {
        width: 60,
        height: 60,
        borderRadius: 16,
        backgroundColor: colors.white,
        alignItems: 'center',
        justifyContent: 'center',
    },
    avatarText: { color: colors.blue, fontSize: 28, fontWeight: '700' },
    profileLabel: { color: 'rgba(255,255,255,0.75)', fontSize: 15 },
    profileName: { color: colors.white, fontSize: 24, fontWeight: '600' },
    tilesRow: { flexDirection: 'row', gap: 12, marginBottom: 24 },
    tile: {
        flex: 1,
        alignItems: 'center',
        gap: 4,
        backgroundColor: 'rgba(255,255,255,0.15)',
        borderRadius: 16,
        paddingVertical: 14,
    },
    tileValue: { color: colors.white, fontSize: 28, fontWeight: '700' },
    tileLabel: { color: 'rgba(255,255,255,0.8)', fontSize: 14 },
    item: {
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: colors.white,
        borderRadius: 16,
        padding: 14,
        marginBottom: 12,
    },
    itemIcon: {
        width: 44,
        height: 44,
        borderRadius: 12,
        backgroundColor: colors.blueBg,
        alignItems: 'center',
        justifyContent: 'center',
    },
    itemIconDanger: { backgroundColor: colors.orangeBg },
    itemTexts: { flex: 1, marginLeft: 14 },
    itemLabel: { fontSize: 19, fontWeight: '600' },
    itemSubtitle: { color: colors.gray, fontSize: 15, marginTop: 2 },
});
