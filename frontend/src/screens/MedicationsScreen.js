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
import { listSchedules } from '../services/scheduleService';
import { getDailyTimes, joinTimes, STRIPE_LABELS } from '../utils/format';

// Cada tipo de tarja tem a sua cor, como na caixa do remédio.
const STRIPE_COLORS = {
  NONE: { bg: colors.blueBg, text: colors.blue },
  YELLOW: { bg: '#FFD54F', text: '#4E3B00' },
  RED: { bg: '#C62828', text: colors.white },
  BLACK: { bg: '#1A1A1A', text: colors.white },
  OTHER: { bg: '#E6E6E6', text: colors.gray },
};

function StripeTag({ stripe }) {
  const key = STRIPE_COLORS[stripe] ? stripe : 'OTHER';
  const palette = STRIPE_COLORS[key];
  return (
    <View style={[styles.tag, { backgroundColor: palette.bg }]}>
      <Text style={[styles.tagText, { color: palette.text }]}>
        {STRIPE_LABELS[key]}
      </Text>
    </View>
  );
}

export default function MedicationsScreen({ navigation, route }) {
  const profile = getActiveProfile();

  const [medications, setMedications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  async function loadMedications() {
    if (!profile) {
      setLoading(false);
      return;
    }
    setLoading(true);
    setError('');
    try {
      const list = await listSchedules(profile.id, { type: 'MEDICATION' });
      setMedications(list);
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }

  // Recarrega sempre que a tela aparece
  useFocusEffect(
    useCallback(() => {
      loadMedications();
      // Ao sair da tela, apaga a marca "veio da Mais" (a seta só aparece quando vem de lá)
      return () => navigation.setParams({ from: undefined });
    }, [])
  );

  return (
    <View style={styles.container}>
      <Header
        title="Remédios"
        onBack={route.params?.from === 'More' ? () => navigation.navigate('More') : undefined}
      />

      <ScrollView contentContainerStyle={styles.content}>
        {loading && <ActivityIndicator size="large" color={colors.blue} />}

        {error !== '' && <Text style={styles.error}>{error}</Text>}

        {!loading && !error && medications.length === 0 && (
          <Text style={styles.message}>Nenhum remédio cadastrado.</Text>
        )}

        {medications.map((item) => {
          const times = joinTimes(
            getDailyTimes(item.startAt, item.intervalValue, item.intervalUnit)
          );
          return (
            <Pressable
              key={item.id}
              style={styles.card}
              onPress={() => navigation.navigate('MedicationForm', { schedule: item })}
            >
              <View style={styles.pillBox}>
                <MaterialCommunityIcons
                  name="pill"
                  size={30}
                  color={colors.blue}
                />
              </View>
              <View style={styles.cardTexts}>
                <Text style={styles.name}>{item.title}</Text>
                {item.details?.activeIngredient ? (
                  <Text style={styles.detail}>
                    Princípio ativo: {item.details.activeIngredient}
                  </Text>
                ) : null}
                <View style={styles.timeRow}>
                  <Ionicons name="time-outline" size={20} color={colors.gray} />
                  <Text style={styles.detail}>{times}</Text>
                </View>
                <StripeTag stripe={item.details?.stripe} />
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
        <Pressable style={styles.addButton} onPress={() => navigation.navigate('MedicationForm')}>
          <Ionicons name="add" size={26} color={colors.white} />
          <Text style={styles.addText}>Novo remédio</Text>
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
  pillBox: {
    width: 64,
    height: 64,
    borderRadius: 14,
    backgroundColor: colors.blueBg,
    alignItems: 'center',
    justifyContent: 'center',
  },
  cardTexts: { flex: 1, marginLeft: 14 },
  name: { color: colors.ink, fontSize: 22, fontWeight: '600' },
  detail: { color: colors.gray, fontSize: 17, marginTop: 4 },
  timeRow: { flexDirection: 'row', alignItems: 'center', gap: 6, marginTop: 2 },
  tag: {
    alignSelf: 'flex-start',
    paddingVertical: 4,
    paddingHorizontal: 14,
    borderRadius: 14,
    marginTop: 10,
  },
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
  addText: {
    color: colors.white,
    fontSize: 20,
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