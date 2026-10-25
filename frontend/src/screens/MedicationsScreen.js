import { Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { MaterialCommunityIcons, Ionicons } from '@expo/vector-icons';
import Header from '../components/Header';
import { colors } from '../styles/theme';
import { mockMedications } from '../services/mockData';
import { getDailyTimes, joinTimes, STRIPE_LABELS } from '../utils/format';

function StripeTag({ stripe }) {
  const isNone = stripe === 'NONE';
  return (
    <View style={[styles.tag, isNone ? styles.tagNone : styles.tagStripe]}>
      <Text
        style={[styles.tagText, { color: isNone ? colors.blue : colors.orangeText }]}
      >
        {STRIPE_LABELS[stripe] ?? STRIPE_LABELS.OTHER}
      </Text>
    </View>
  );
}

export default function MedicationsScreen() {
  return (
    <View style={styles.container}>
      <Header title="Remédios" />

      <ScrollView contentContainerStyle={styles.content}>
        {mockMedications.map((item) => {
          const times = joinTimes(
            getDailyTimes(item.startAt, item.intervalValue, item.intervalUnit)
          );
          return (
            <View key={item.id} style={styles.card}>
              <View style={styles.pillBox}>
                <MaterialCommunityIcons
                  name="pill"
                  size={30}
                  color={colors.blue}
                />
              </View>
              <View style={styles.cardTexts}>
                <Text style={styles.name}>{item.title}</Text>
                <Text style={styles.detail}>
                  Princípio ativo: {item.details.activeIngredient}
                </Text>
                <View style={styles.timeRow}>
                  <Ionicons name="time-outline" size={20} color={colors.gray} />
                  <Text style={styles.detail}>{times}</Text>
                </View>
                <StripeTag stripe={item.details.stripe} />
              </View>
            </View>
          );
        })}
      </ScrollView>

      <View style={styles.footer}>
        <Pressable style={styles.addButton}>
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
  tagNone: { backgroundColor: colors.blueBg },
  tagStripe: { backgroundColor: colors.orangeBg },
  tagText: { fontSize: 15, fontWeight: '600' },
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
});