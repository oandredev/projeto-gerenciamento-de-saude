import { Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { MaterialCommunityIcons, Ionicons } from '@expo/vector-icons';
import Header from '../components/Header';
import { colors } from '../styles/theme';
import { mockAppointments } from '../services/mockData';
import { formatDate, formatTime } from '../utils/format';

export default function AppointmentsScreen() {
  return (
    <View style={styles.container}>
      <Header title="Consultas" />

      <ScrollView contentContainerStyle={styles.content}>
        {mockAppointments.map((item) => (
          <View key={item.id} style={styles.card}>
            <View style={styles.iconBox}>
              <MaterialCommunityIcons
                name="stethoscope"
                size={30}
                color={colors.blue}
              />
            </View>
            <View style={styles.cardTexts}>
              <Text style={styles.doctor}>{item.details.doctorName}</Text>
              <View style={styles.row}>
                <Ionicons name="location-outline" size={20} color={colors.gray} />
                <Text style={styles.detail}>{item.details.location}</Text>
              </View>
              <View style={styles.row}>
                <Ionicons name="calendar-outline" size={20} color={colors.gray} />
                <Text style={styles.detail}>
                  {formatDate(item.startAt)} · {formatTime(item.startAt)}
                </Text>
              </View>
            </View>
          </View>
        ))}
      </ScrollView>

      <View style={styles.footer}>
        <Pressable style={styles.addButton}>
          <Ionicons name="add" size={26} color={colors.white} />
          <Text style={styles.addText}>Nova consulta</Text>
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
  doctor: { color: colors.ink, fontSize: 22, fontWeight: '600' },
  row: { flexDirection: 'row', alignItems: 'center', gap: 6, marginTop: 6 },
  detail: { color: colors.gray, fontSize: 17 },
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