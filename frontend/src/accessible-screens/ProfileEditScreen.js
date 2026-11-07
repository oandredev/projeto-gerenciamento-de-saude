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
import { colors } from '../styles/theme';
import {
  getActiveProfile,
  setActiveProfile,
  updateProfile,
} from '../services/profileService';
import { AVATARS, getAvatar } from '../utils/avatars';

export default function ProfileEditScreen({ navigation }) {
  const profile = getActiveProfile();

  const [name, setName] = useState(profile?.name ?? '');
  const [avatarId, setAvatarId] = useState(profile?.avatarId ?? 1);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  async function handleSave() {
    const cleanName = name.trim();

    if (!profile) {
      setError('Nenhum perfil escolhido.');
      return;
    }
    if (cleanName.length < 3) {
      setError('O nome precisa ter pelo menos 3 letras.');
      return;
    }

    setError('');
    setSaving(true);
    try {
      // O PUT substitui tudo: name, avatarId e active são obrigatórios
      const updated = await updateProfile(profile.id, {
        name: cleanName,
        avatarId,
        active: true,
      });
      // Guarda o perfil atualizado para a tela Mais mostrar o nome e a cor novos
      setActiveProfile(updated);
      navigation.goBack();
    } catch (e) {
      setError(e.message);
    } finally {
      setSaving(false);
    }
  }

  // O back-end não tem DELETE: arquivar é um PUT com active = false.
  async function archive() {
    setError('');
    setSaving(true);
    try {
      await updateProfile(profile.id, {
        name: profile.name,
        avatarId: profile.avatarId,
        active: false,
      });
      // O perfil arquivado não pode continuar em uso: volta para a escolha de perfis
      setActiveProfile(null);
      navigation.reset({ index: 0, routes: [{ name: 'ProfileSelect' }] });
    } catch (e) {
      setError(e.message);
      setSaving(false);
    }
  }

  function handleArchive() {
    Alert.alert(
      'Arquivar perfil',
      'Ele deixará de aparecer na escolha de perfis. Deseja continuar?',
      [
        { text: 'Cancelar', style: 'cancel' },
        { text: 'Arquivar', style: 'destructive', onPress: archive },
      ]
    );
  }

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <Header
        title="Editar perfil"
        showProfileSwitch={false}
        onBack={() => navigation.goBack()}
      />

      <ScrollView
        contentContainerStyle={styles.content}
        keyboardShouldPersistTaps="handled"
      >
        {/* Prévia: decorativa, escondida do leitor de tela */}
        <View
          style={[styles.preview, { backgroundColor: getAvatar(avatarId).bg }]}
          accessibilityElementsHidden
          importantForAccessibility="no-hide-descendants"
        >
          <Text style={styles.previewLetter}>
            {name.trim().charAt(0).toUpperCase() || '?'}
          </Text>
        </View>

        <Text style={styles.label}>Nome</Text>
        <TextInput
          style={styles.input}
          placeholder="Ex.: Mãe"
          placeholderTextColor={colors.placeholder}
          value={name}
          onChangeText={setName}
          accessible
          accessibilityLabel="Nome do perfil"
        />

        <Text style={styles.label}>Cor do avatar</Text>
        <View style={styles.avatarRow} accessibilityRole="radiogroup">
          {AVATARS.map((a, index) => (
            <Pressable
              key={a.id}
              style={[
                styles.avatarChoice,
                { backgroundColor: a.bg },
                avatarId === a.id && styles.avatarChosen,
              ]}
              onPress={() => setAvatarId(a.id)}
              accessible
              accessibilityRole="radio"
              accessibilityLabel={`Cor ${index + 1} de ${AVATARS.length}`}
              accessibilityState={{ checked: avatarId === a.id }}
            >
              {avatarId === a.id && (
                <Ionicons name="checkmark" size={22} color={colors.white} />
              )}
            </Pressable>
          ))}
        </View>

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
          accessibilityLabel="Salvar edição do perfil"
          accessibilityState={{ disabled: saving, busy: saving }}
        >
          <Text style={styles.saveText}>
            {saving ? 'Salvando...' : 'Salvar edição'}
          </Text>
        </Pressable>

        <Pressable
          style={styles.archiveButton}
          onPress={handleArchive}
          disabled={saving}
          accessible
          accessibilityRole="button"
          accessibilityLabel="Arquivar perfil"
          accessibilityHint="Pede confirmação antes de arquivar"
          accessibilityState={{ disabled: saving }}
        >
          <Text style={styles.archiveText}>Arquivar perfil</Text>
        </Pressable>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: colors.white },
  content: { padding: 24, paddingBottom: 40 },
  preview: {
    width: 100,
    height: 100,
    borderRadius: 22,
    alignSelf: 'center',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 28,
  },
  previewLetter: { color: colors.white, fontSize: 46, fontWeight: '700' },
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
  avatarRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 12,
    marginBottom: 20,
  },
  avatarChoice: {
    width: 48,
    height: 48,
    borderRadius: 24,
    alignItems: 'center',
    justifyContent: 'center',
  },
  avatarChosen: { borderWidth: 3, borderColor: colors.ink },
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
  archiveButton: {
    height: 56,
    borderRadius: 28,
    borderWidth: 1.5,
    borderColor: colors.orangeText,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 12,
  },
  archiveText: { color: colors.orangeText, fontSize: 18, fontWeight: '600' },
});
