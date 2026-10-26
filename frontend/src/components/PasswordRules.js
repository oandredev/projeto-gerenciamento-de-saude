import { StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../styles/theme';
import { getPasswordRules } from '../utils/validation';

export default function PasswordRules({ password }) {
  const rules = getPasswordRules(password);

  return (
    <View style={styles.box}>
      <Text style={styles.title}>A senha precisa ter:</Text>
      {rules.map((rule) => (
        <View key={rule.id} style={styles.row}>
          <Ionicons
            name={rule.ok ? 'checkmark-circle' : 'ellipse-outline'}
            size={20}
            color={rule.ok ? colors.blue : colors.placeholder}
          />
          <Text style={[styles.text, rule.ok && styles.textOk]}>
            {rule.label}
          </Text>
        </View>
      ))}
    </View>
  );
}

const styles = StyleSheet.create({
  box: {
    backgroundColor: colors.blueBg,
    borderRadius: 14,
    padding: 14,
    marginBottom: 20,
  },
  title: {
    color: colors.blue,
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 8,
  },
  row: { flexDirection: 'row', alignItems: 'center', gap: 8, marginTop: 4 },
  text: { color: colors.gray, fontSize: 16 },
  textOk: { color: colors.blue, fontWeight: '600' },
});