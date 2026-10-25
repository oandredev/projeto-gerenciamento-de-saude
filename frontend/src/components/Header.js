import { Pressable, StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import Logo from './Logo';
import { colors } from '../styles/theme';

export default function Header({ title, onBack, showProfileSwitch = true }) {
  return (
    <View>
      <View style={styles.container}>
        <View style={styles.topRow}>
          <View style={styles.leftGroup}>
            {onBack && (
              <Pressable style={styles.circleButton} onPress={onBack}>
                <Ionicons name="arrow-back" size={24} color={colors.white} />
              </Pressable>
            )}
            <Logo size={44} />
          </View>
          <Pressable style={styles.circleButton}>
            <Ionicons
              name="notifications-outline"
              size={24}
              color={colors.white}
            />
          </Pressable>
        </View>

        <Text style={styles.title}>{title}</Text>

        {showProfileSwitch && (
          <Pressable style={styles.profileButton}>
            <Ionicons
              name="swap-horizontal"
              size={20}
              color={colors.orangeLight}
            />
            <Text style={styles.profileText}>Trocar perfil</Text>
          </Pressable>
        )}
      </View>
      <View style={styles.line} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: colors.blue,
    paddingTop: 56,
    paddingHorizontal: 24,
    paddingBottom: 20,
  },
  topRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  leftGroup: { flexDirection: 'row', alignItems: 'center', gap: 12 },
  circleButton: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: 'rgba(255,255,255,0.15)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    color: colors.white,
    fontSize: 28,
    fontWeight: '600',
    marginTop: 16,
  },
  profileButton: {
    flexDirection: 'row',
    alignItems: 'center',
    alignSelf: 'flex-start',
    gap: 8,
    marginTop: 14,
    paddingVertical: 10,
    paddingHorizontal: 16,
    borderRadius: 22,
    backgroundColor: 'rgba(255,255,255,0.15)',
  },
  profileText: { color: colors.white, fontSize: 17 },
  line: { height: 3, backgroundColor: colors.orange },
});