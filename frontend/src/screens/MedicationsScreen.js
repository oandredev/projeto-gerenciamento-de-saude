import { Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { MaterialCommunityIcons, Ionicons } from '@expo/vector-icons';
import Header from '../components/Header';
import { colors } from '../styles/theme';

const MEDICATIONS = [
  {
    id: '1',
    name: 'Omeprazol 20 mg',
    activeIngredient: 'omeprazol',
    times: '08:00 e 20:00',
    stripe: 'none',
  },
  {
    id: '2',
    name: 'Metformina 850 mg',
    activeIngredient: 'metformina',
    times: '20:00',
    stripe: 'red',
  },
];

function StripeTag({ stripe }) {
  const isRed = stripe === 'red';
  return (
    <View style={[styles.tag, isRed ? styles.tagRed : styles.tagNone]}>
      <Text style={[styles.tagText, { color: isRed ? colors.orangeText : colors.blue }]}>
        {isRed ? 'Tarja vermelha' : 'Sem tarja'}
      </Text>
    </View>
  );
}

export default function MedicationsScreen() {
  return (
    <View style={styles.container}>
      <Header title="Remédios" />

      <ScrollView contentContainerStyle={styles.content}>
        {MEDICATIONS.map((item) => (
          <View key={item.id} style={styles.card}>
            <View style={styles.pillBox}>
              <MaterialCommunityIcons name="pill" size={30} color={colors.blue} />
            </View>
            <View style={styles.cardTexts}>
              <Text style={styles.name}>{item.name}</Text>
              <Text style={styles.detail}>
                Princípio ativo: {item.activeIngredient}
              </Text>
              <View style={styles.timeRow}>
                <Ionicons name="time-outline" size={20} color={colors.gray} />
                <Text style={styles.detail}>{item.times}</Text>
              </View>
              <StripeTag stripe={item.stripe} />
            </View>
          </View>
        ))}
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
  tagRed: { backgroundColor: colors.orangeBg },
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