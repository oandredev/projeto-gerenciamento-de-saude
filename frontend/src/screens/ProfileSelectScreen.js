import { useCallback, useState } from 'react';
import {
    ActivityIndicator,
    Modal,
    Pressable,
    ScrollView,
    StyleSheet,
    Text,
    TextInput,
    View,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useFocusEffect } from '@react-navigation/native';
import { colors } from '../styles/theme';
import {
    listProfiles,
    createProfile,
    setActiveProfile,
} from '../services/profileService';
import { AVATARS, getAvatar } from '../utils/avatars';

export default function ProfileSelectScreen({ navigation }) {
    const [profiles, setProfiles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const [showForm, setShowForm] = useState(false);
    const [name, setName] = useState('');
    const [avatarId, setAvatarId] = useState(1);
    const [formError, setFormError] = useState('');
    const [saving, setSaving] = useState(false);

    async function loadProfiles() {
        setLoading(true);
        setError('');
        try {
            const list = await listProfiles();
            setProfiles(list.filter((p) => p.active));
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }

    // Recarrega a lista sempre que a tela aparece
    useFocusEffect(
        useCallback(() => {
            loadProfiles();
        }, [])
    );

    function handleSelect(profile) {
        setActiveProfile(profile);
        navigation.reset({ index: 0, routes: [{ name: 'MainTabs' }] });
    }

    function openForm() {
        setName('');
        setAvatarId(1);
        setFormError('');
        setShowForm(true);
    }

    async function handleCreate() {
        if (!name.trim()) {
            setFormError('Digite o nome do perfil.');
            return;
        }
        setFormError('');
        setSaving(true);
        try {
            await createProfile({ name: name.trim(), avatarId });
            setShowForm(false);
            await loadProfiles();
        } catch (e) {
            setFormError(e.message);
        } finally {
            setSaving(false);
        }
    }

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Quem você quer acompanhar?</Text>

            {loading ? (
                <ActivityIndicator size="large" color={colors.white} />
            ) : (
                <ScrollView contentContainerStyle={styles.grid}>
                    {error !== '' && <Text style={styles.error}>{error}</Text>}

                    {profiles.map((profile) => (
                        <Pressable
                            key={profile.id}
                            style={styles.tile}
                            onPress={() => handleSelect(profile)}
                        >
                            <View
                                style={[
                                    styles.avatar,
                                    { backgroundColor: getAvatar(profile.avatarId).bg },
                                ]}
                            >
                                <Text style={styles.avatarLetter}>
                                    {profile.name.charAt(0).toUpperCase()}
                                </Text>
                            </View>
                            <Text style={styles.tileName} numberOfLines={1}>
                                {profile.name}
                            </Text>
                        </Pressable>
                    ))}

                    <Pressable style={styles.tile} onPress={openForm}>
                        <View style={[styles.avatar, styles.avatarAdd]}>
                            <Ionicons name="add" size={56} color={colors.white} />
                        </View>
                        <Text style={styles.tileName}>Adicionar perfil</Text>
                    </Pressable>
                </ScrollView>
            )}

            <Modal visible={showForm} transparent animationType="slide">
                <View style={styles.overlay}>
                    <View style={styles.card}>
                        <Text style={styles.cardTitle}>Novo perfil</Text>

                        <Text style={styles.label}>Nome</Text>
                        <TextInput
                            style={styles.input}
                            placeholder="Ex.: Mãe"
                            placeholderTextColor={colors.placeholder}
                            value={name}
                            onChangeText={setName}
                        />

                        <Text style={styles.label}>Cor do avatar</Text>
                        <View style={styles.avatarRow}>
                            {AVATARS.map((a) => (
                                <Pressable
                                    key={a.id}
                                    style={[
                                        styles.avatarChoice,
                                        { backgroundColor: a.bg },
                                        avatarId === a.id && styles.avatarChosen,
                                    ]}
                                    onPress={() => setAvatarId(a.id)}
                                >
                                    {avatarId === a.id && (
                                        <Ionicons name="checkmark" size={22} color={colors.white} />
                                    )}
                                </Pressable>
                            ))}
                        </View>

                        {formError !== '' && <Text style={styles.error}>{formError}</Text>}

                        <Pressable
                            style={styles.saveButton}
                            onPress={handleCreate}
                            disabled={saving}
                        >
                            <Text style={styles.saveText}>
                                {saving ? 'Salvando...' : 'Salvar'}
                            </Text>
                        </Pressable>
                        <Pressable
                            style={styles.cancelButton}
                            onPress={() => setShowForm(false)}
                        >
                            <Text style={styles.cancelText}>Cancelar</Text>
                        </Pressable>
                    </View>
                </View>
            </Modal>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: colors.blue,
        paddingTop: 90,
    },
    title: {
        color: colors.white,
        fontSize: 28,
        fontWeight: '700',
        textAlign: 'center',
        paddingHorizontal: 24,
        marginBottom: 32,
    },
    grid: {
        flexDirection: 'row',
        flexWrap: 'wrap',
        justifyContent: 'center',
        gap: 24,
        paddingHorizontal: 24,
        paddingBottom: 40,
    },
    tile: { width: 130, alignItems: 'center' },
    avatar: {
        width: 120,
        height: 120,
        borderRadius: 24,
        alignItems: 'center',
        justifyContent: 'center',
    },
    avatarAdd: {
        backgroundColor: 'transparent',
        borderWidth: 3,
        borderColor: colors.white,
        borderStyle: 'dashed',
    },
    avatarLetter: { color: colors.white, fontSize: 54, fontWeight: '700' },
    tileName: {
        color: colors.white,
        fontSize: 18,
        fontWeight: '600',
        marginTop: 10,
    },
    error: {
        color: colors.orangeText,
        fontSize: 16,
        fontWeight: '600',
        marginBottom: 12,
    },
    overlay: {
        flex: 1,
        backgroundColor: 'rgba(0,0,0,0.5)',
        justifyContent: 'flex-end',
    },
    card: {
        backgroundColor: colors.white,
        borderTopLeftRadius: 24,
        borderTopRightRadius: 24,
        padding: 24,
        paddingBottom: 40,
    },
    cardTitle: {
        color: colors.blue,
        fontSize: 22,
        fontWeight: '700',
        marginBottom: 20,
    },
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
        marginBottom: 20,
    },
    avatarRow: { flexDirection: 'row', gap: 10, marginBottom: 20 },
    avatarChoice: {
        width: 44,
        height: 44,
        borderRadius: 22,
        alignItems: 'center',
        justifyContent: 'center',
    },
    avatarChosen: { borderWidth: 3, borderColor: colors.ink },
    saveButton: {
        height: 56,
        borderRadius: 28,
        backgroundColor: colors.blue,
        alignItems: 'center',
        justifyContent: 'center',
    },
    saveText: { color: colors.white, fontSize: 20, fontWeight: '600' },
    cancelButton: { alignItems: 'center', paddingVertical: 16 },
    cancelText: { color: colors.blue, fontSize: 18, fontWeight: '600' },
});