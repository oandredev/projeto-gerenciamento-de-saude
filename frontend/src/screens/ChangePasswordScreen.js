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
import { changePassword } from '../services/authService';

// Campo de senha com o "olhinho" para mostrar/ocultar
function PasswordField({ label, placeholder, value, onChangeText }) {
  const [visible, setVisible] = useState(false);

  return (
    <>
      <Text style={styles.label}>{label}</Text>
      <View style={styles.passwordBox}>
        <TextInput
          style={styles.passwordInput}
          placeholder={placeholder}
          placeholderTextColor={colors.placeholder}
          secureTextEntry={!visible}
          autoCapitalize="none"
          autoCorrect={false}
          value={value}
          onChangeText={onChangeText}
        />
        <Pressable onPress={() => setVisible((v) => !v)} hitSlop={10}>
          <Ionicons
            name={visible ? 'eye-off-outline' : 'eye-outline'}
            size={24}
            color={colors.gray}
          />
        </Pressable>
      </View>
    </>
  );
}

export default function ChangePasswordScreen({ navigation }) {
  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  async function handleSave() {
    if (!currentPassword || !newPassword || !confirmPassword) {
      setError('Preencha todos os campos.');
      return;
    }
    if (newPassword.length < 8) {
      setError('A nova senha precisa ter pelo menos 8 caracteres.');
      return;
    }
    if (newPassword !== confirmPassword) {
      setError('A confirmação não é igual à nova senha.');
      return;
    }

    setError('');
    setSaving(true);
    try {
      await changePassword({ currentPassword, newPassword });
      Alert.alert('Senha alterada', 'Sua senha foi alterada com sucesso.', [
        { text: 'OK', onPress: () => navigation.goBack() },
      ]);
    } catch (e) {
      // O back-end valida a senha atual e as regras da nova senha
      setError(e.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <Header
        title="Trocar senha"
        showProfileSwitch={false}
        onBack={() => navigation.goBack()}
      />

      <ScrollView
        contentContainerStyle={styles.content}
        keyboardShouldPersistTaps="handled"
      >
        <PasswordField
          label="Senha atual"
          placeholder="Digite a senha atual"
          value={currentPassword}
          onChangeText={setCurrentPassword}
        />

        <PasswordField
          label="Nova senha"
          placeholder="Digite a nova senha"
          value={newPassword}
          onChangeText={setNewPassword}
        />

        <PasswordField
          label="Confirmar nova senha"
          placeholder="Repita a nova senha"
          value={confirmPassword}
          onChangeText={setConfirmPassword}
        />

        <Text style={styles.hint}>
          Use pelo menos 8 caracteres, com letra minúscula, maiúscula, número e
          símbolo.
        </Text>

        {error !== '' && <Text style={styles.error}>{error}</Text>}

        <Pressable
          style={styles.saveButton}
          onPress={handleSave}
          disabled={saving}
        >
          <Text style={styles.saveText}>
            {saving ? 'Salvando...' : 'Trocar senha'}
          </Text>
          {!saving && (
            <Ionicons name="checkmark" size={24} color={colors.orangeLight} />
          )}
        </Pressable>
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
  passwordBox: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 56,
    borderWidth: 1.5,
    borderColor: colors.border,
    borderRadius: 14,
    paddingHorizontal: 16,
    backgroundColor: colors.white,
    marginBottom: 20,
  },
  passwordInput: { flex: 1, fontSize: 18, color: colors.ink },
  hint: { color: colors.gray, fontSize: 15, marginBottom: 20 },
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
});