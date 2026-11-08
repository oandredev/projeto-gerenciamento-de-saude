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
import { listRestrictions } from '../services/restrictionService';
import { RESTRICTION_TYPES, RESTRICTION_SEVERITIES } from '../utils/restrictions';

function SeverityTag({ severity }) {
  const label = RESTRICTION_SEVERITIES.find((s) => s.value === severity)?.label ?? severity;
  const isSevere = severity === 'SEVERY';
  const isMild = severity === 'MILD';
  return (
    <View
      style={[
        styles.tag,
        isMild && styles.tagMild,
        severity === 'MODERATE' && styles.tagModerate,
        isSevere && styles.tagSevere,
      ]}
    >
      <Text
        style={[
          styles.tagText,
          { color: isSevere ? colors.white : isMild ? colors.blue : colors.orangeText },
        ]}
      >
        {label}
      </Text>
    </View>
  );
}

export default function RestrictionsScreen({ navigation }) {
  const profile = getActiveProfile();

  const [restrictions, setRestrictions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  async function loadRestrictions() {
    if (!profile) {
      setLoading(false);
      return;
    }
    setLoading(true);
    setError('');
    try {
      const list = await listRestrictions(profile.id);
      // O back-end devolve também as arquivadas: mostramos só as ativas
      setRestrictions(list.filter((r) => r.active));
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }

  // Recarrega sempre que a tela aparece
  useFocusEffect(
    useCallback(() => {
      loadRestrictions();
    }, [])
  );

  return (
    <View style={styles.container}>
      <Header
        title="Restrições"
        showProfileSwitch={false}
        onBack={() => navigation.goBack()}
      />

      <ScrollView contentContainerStyle={styles.content}>
        {loading && <ActivityIndicator size="large" color={colors.blue} />}

        {error !== '' && <Text style={styles.error}>{error}</Text>}

        {!loading && !error && restrictions.length === 0 && (
          <Text style={styles.message}>Nenhuma restrição cadastrada.</Text>
        )}

        {restrictions.map((item) => {
          const type = RESTRICTION_TYPES.find((t) => t.value === item.type);
          return (
            <Pressable
              key={item.id}
              style={styles.card}
              onPress={() => navigation.navigate('RestrictionForm', { restriction: item })}
            >
              <View style={styles.iconBox}>
                <MaterialCommunityIcons
                  name={type?.icon ?? 'alert-circle-outline'}
                  size={30}
                  color={colors.blue}
                />
              </View>
              <View style={styles.cardTexts}>
                <Text style={styles.typeLabel}>{type?.label ?? item.type}</Text>
                <Text style={styles.title}>{item.title}</Text>
                {item.note ? <Text style={styles.note}>{item.note}</Text> : null}
                <SeverityTag severity={item.severity} />

                {/* Aviso de que o card pode ser tocado para editar */}
                <View style={styles.editRow}>
                  <Ionicons name="create-outline" size={18} color={colors.blue} />
                  <Text style={styles.editText}>Toque para editar</Text>
                </View>
              </View>
            </Pressable>
          );
        })}
      </ScrollView>

      <View style={styles.footer}>
        <Pressable
          style={styles.addButton}
          onPress={() => navigation.navigate('RestrictionForm')}
        >
          <Ionicons name="add" size={26} color={colors.white} />
          <Text style={styles.addText}>Nova restrição</Text>
        </Pressable>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: colors.white },
  content: { padding: 24, paddingBottom: 16 },
  card: {
    flexDirection: 'row',
    borderWidth: 1.5,
    borderColor: colors.border,
    borderRadius: 16,
    padding: 16,
    marginBottom: 14,
  },
  iconBox: {
    width: 64,
    height: 64,
    borderRadius: 14,
    backgroundColor: colors.blueBg,
    alignItems: 'center',
    justifyContent: 'center',
  },
  cardTexts: { flex: 1, marginLeft: 14 },
  typeLabel: { color: colors.orangeText, fontSize: 14, fontWeight: '600' },
  title: { color: colors.ink, fontSize: 22, fontWeight: '600' },
  note: { color: colors.gray, fontSize: 16, marginTop: 4 },
  tag: {
    alignSelf: 'flex-start',
    paddingVertical: 4,
    paddingHorizontal: 14,
    borderRadius: 14,
    marginTop: 10,
  },
  tagMild: { backgroundColor: colors.blueBg },
  tagModerate: { backgroundColor: colors.orangeBg },
  tagSevere: { backgroundColor: colors.orangeText },
  tagText: { fontSize: 15, fontWeight: '600' },
  editRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    marginTop: 12,
  },
  editText: { color: colors.blue, fontSize: 15, fontWeight: '600' },
  footer: { padding: 24, paddingTop: 8 },
  addButton: {
    height: 56,
    borderRadius: 28,
    backgroundColor: colors.blue,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
  },
  addText: { color: colors.white, fontSize: 20, fontWeight: '600' },
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