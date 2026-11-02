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
import { formatDate, formatTime } from '../utils/format';

export default function AppointmentsScreen({ navigation }) {
  const profile = getActiveProfile();

  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  async function loadAppointments() {
    if (!profile) {
      setLoading(false);
      return;
    }
    setLoading(true);
    setError('');
    try {
      const list = await listSchedules(profile.id);
      setAppointments(
        list
          .filter((s) => s.type === 'CONSULTATION' || s.type === 'EXAM')
          .sort((a, b) => new Date(a.startAt) - new Date(b.startAt))
      );
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }

  // Recarrega sempre que a tela aparece
  useFocusEffect(
    useCallback(() => {
      loadAppointments();
    }, [])
  );

  return (
    <View style={styles.container}>
      <Header title="Consultas" />

      <ScrollView contentContainerStyle={styles.content}>
        {loading && <ActivityIndicator size="large" color={colors.blue} />}

        {error !== '' && <Text style={styles.error}>{error}</Text>}

        {!loading && !error && appointments.length === 0 && (
          <Text style={styles.message}>Nenhuma consulta ou exame cadastrado.</Text>
        )}

        {appointments.map((item) => (
          <Pressable
            key={item.id}
            style={styles.card}
            onPress={() => navigation.navigate('AppointmentForm', { schedule: item })}
          >
            <View style={styles.iconBox}>
              <MaterialCommunityIcons
                name={item.type === 'EXAM' ? 'clipboard-text-outline' : 'stethoscope'}
                size={30}
                color={colors.blue}
              />
            </View>
            <View style={styles.cardTexts}>
              <Text style={styles.typeLabel}>
                {item.type === 'EXAM' ? 'Exame' : 'Consulta'}
              </Text>
              <Text style={styles.title}>{item.title}</Text>
              {item.details?.doctorName ? (
                <View style={styles.row}>
                  <Ionicons name="person-outline" size={20} color={colors.gray} />
                  <Text style={styles.detail}>{item.details.doctorName}</Text>
                </View>
              ) : null}
              {item.details?.location ? (
                <View style={styles.row}>
                  <Ionicons name="location-outline" size={20} color={colors.gray} />
                  <Text style={styles.detail}>{item.details.location}</Text>
                </View>
              ) : null}
              <View style={styles.row}>
                <Ionicons name="calendar-outline" size={20} color={colors.gray} />
                <Text style={styles.detail}>
                  {formatDate(item.startAt)} · {formatTime(item.startAt)}
                </Text>
              </View>
            </View>
          </Pressable>
        ))}
      </ScrollView>

      <View style={styles.footer}>
        <Pressable
          style={styles.addButton}
          onPress={() => navigation.navigate('AppointmentForm')}
        >
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
  typeLabel: { color: colors.orangeText, fontSize: 14, fontWeight: '600' },
  title: { color: colors.ink, fontSize: 22, fontWeight: '600' },
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