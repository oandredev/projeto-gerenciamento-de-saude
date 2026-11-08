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
import { getActiveProfile } from '../services/profileService';
import { createRestriction, updateRestriction } from '../services/restrictionService';
import { RESTRICTION_TYPES, RESTRICTION_SEVERITIES } from '../utils/restrictions';

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

export default function RestrictionFormScreen({ navigation, route }) {
  // Se veio um item da lista, estamos editando
  const restriction = route.params?.restriction;
  const isEditing = !!restriction;

  const [type, setType] = useState(restriction?.type ?? 'FOOD');
  const [severity, setSeverity] = useState(restriction?.severity ?? 'MODERATE');
  const [title, setTitle] = useState(restriction?.title ?? '');
  const [note, setNote] = useState(restriction?.note ?? '');
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  async function handleSave() {
    const profile = getActiveProfile();
    const cleanTitle = title.trim();

    if (cleanTitle.length < 3) {
      setError('O nome precisa ter pelo menos 3 letras.');
      return;
    }
    if (!profile) {
      setError('Escolha um perfil antes de cadastrar.');
      return;
    }

    const body = {
      profileId: isEditing ? restriction.profileId : profile.id,
      type,
      severity,
      title: cleanTitle,
      note: note.trim() || undefined,
    };

    setError('');
    setSaving(true);
    try {
      if (isEditing) {
        await updateRestriction(restriction.id, { ...body, active: true });
      } else {
        await createRestriction(body);
      }
      navigation.goBack();
    } catch (e) {
      setError(e.message);
    } finally {
      setSaving(false);
    }
  }

  // O back-end não tem DELETE: arquivar é um PUT com active = false.
  // Mandamos os dados originais, para não falhar por causa de algo que ficou pela metade no formulário.
  async function archive() {
    setError('');
    setSaving(true);
    try {
      await updateRestriction(restriction.id, {
        profileId: restriction.profileId,
        type: restriction.type,
        severity: restriction.severity,
        title: restriction.title,
        note: restriction.note || undefined,
        active: false,
      });
      navigation.goBack();
    } catch (e) {
      setError(e.message);
    } finally {
      setSaving(false);
    }
  }

  function handleArchive() {
    Alert.alert(
      'Arquivar restrição',
      'Ela deixará de aparecer na lista. Deseja continuar?',
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
        title={isEditing ? 'Editar restrição' : 'Nova restrição'}
        showProfileSwitch={false}
        onBack={() => navigation.goBack()}
      />

      <ScrollView
        contentContainerStyle={styles.content}
        keyboardShouldPersistTaps="handled"
      >
        <Text style={styles.label}>Tipo</Text>
        <View style={styles.chipRow}>
          {RESTRICTION_TYPES.map((t) => (
            <Chip
              key={t.value}
              label={t.label}
              selected={type === t.value}
              onPress={() => setType(t.value)}
            />
          ))}
        </View>

        <Text style={styles.label}>Gravidade</Text>
        <View style={styles.chipRow}>
          {RESTRICTION_SEVERITIES.map((s) => (
            <Chip
              key={s.value}
              label={s.label}
              selected={severity === s.value}
              onPress={() => setSeverity(s.value)}
            />
          ))}
        </View>

        <Text style={styles.label}>Nome</Text>
        <TextInput
          style={styles.input}
          placeholder="Ex.: Alergia a amendoim"
          placeholderTextColor={colors.placeholder}
          maxLength={50}
          value={title}
          onChangeText={setTitle}
        />

        <Text style={styles.label}>Observação (opcional)</Text>
        <TextInput
          style={[styles.input, styles.noteInput]}
          placeholder="Ex.: causa inchaço nos lábios"
          placeholderTextColor={colors.placeholder}
          multiline
          maxLength={1000}
          value={note}
          onChangeText={setNote}
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

        {isEditing && (
          <Pressable
            style={styles.archiveButton}
            onPress={handleArchive}
            disabled={saving}
          >
            <Ionicons name="archive-outline" size={20} color={colors.orangeText} />
            <Text style={styles.archiveText}>Arquivar restrição</Text>
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
  noteInput: {
    height: 110,
    paddingTop: 14,
    textAlignVertical: 'top',
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
  archiveButton: {
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
  archiveText: { color: colors.orangeText, fontSize: 18, fontWeight: '600' },
});