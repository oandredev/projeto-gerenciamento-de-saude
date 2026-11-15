import { useState } from 'react';
import {
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
import { getCurrentUser, updateAccount } from '../services/authService';

export default function AccountEditScreen({ navigation }) {
  const user = getCurrentUser();

  const [name, setName] = useState(user?.name ?? '');
  const [email, setEmail] = useState(user?.email ?? '');
  const [currentPassword, setCurrentPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  // O back-end só pede a senha atual quando o e-mail é alterado
  const emailChanged = email.trim() !== (user?.email ?? '');

  async function handleSave() {
    const cleanName = name.trim();
    const cleanEmail = email.trim();

    if (!cleanName || !cleanEmail) {
      setError('Preencha o nome e o e-mail.');
      return;
    }
    if (cleanName.length < 3) {
      setError('O nome precisa ter pelo menos 3 letras.');
      return;
    }
    if (!cleanEmail.includes('@')) {
      setError('O e-mail digitado não é válido.');
      return;
    }
    if (emailChanged && !currentPassword) {
      setError('Para trocar o e-mail, digite a sua senha atual.');
      return;
    }

    setError('');
    setSaving(true);
    try {
      await updateAccount({
        name: cleanName,
        email: cleanEmail,
        currentPassword: emailChanged ? currentPassword : undefined,
      });
      navigation.goBack();
    } catch (e) {
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
        title="Minha conta"
        showProfileSwitch={false}
        onBack={() => navigation.goBack()}
      />

      <ScrollView
        contentContainerStyle={styles.content}
        keyboardShouldPersistTaps="handled"
      >
        <Text style={styles.label}>Nome</Text>
        <TextInput
          style={styles.input}
          placeholder="Seu nome"
          placeholderTextColor={colors.placeholder}
          value={name}
          onChangeText={setName}
        />

        <Text style={styles.label}>E-mail</Text>
        <TextInput
          style={styles.input}
          placeholder="seu@email.com"
          placeholderTextColor={colors.placeholder}
          keyboardType="email-address"
          autoCapitalize="none"
          autoCorrect={false}
          value={email}
          onChangeText={setEmail}
        />

        {/* A senha atual só aparece quando o e-mail foi alterado */}
        {emailChanged && (
          <>
            <Text style={styles.label}>Senha atual</Text>
            <View style={styles.passwordBox}>
              <TextInput
                style={styles.passwordInput}
                placeholder="Digite a senha atual"
                placeholderTextColor={colors.placeholder}
                secureTextEntry={!showPassword}
                autoCapitalize="none"
                autoCorrect={false}
                value={currentPassword}
                onChangeText={setCurrentPassword}
              />
              <Pressable
                onPress={() => setShowPassword((v) => !v)}
                hitSlop={10}
              >
                <Ionicons
                  name={showPassword ? 'eye-off-outline' : 'eye-outline'}
                  size={24}
                  color={colors.gray}
                />
              </Pressable>
            </View>
            <Text style={styles.hint}>
              Por segurança, pedimos a senha atual para trocar o e-mail.
            </Text>
          </>
        )}

        {error !== '' && <Text style={styles.error}>{error}</Text>}

        <Pressable
          style={styles.saveButton}
          onPress={handleSave}
          disabled={saving}
        >
          <Text style={styles.saveText}>
            {saving ? 'Salvando...' : 'Salvar edição'}
          </Text>
          {!saving && (
            <Ionicons name="checkmark" size={24} color={colors.orangeLight} />
          )}
        </Pressable>

        <Pressable
          style={styles.passwordButton}
          onPress={() => navigation.navigate('ChangePassword')}
          disabled={saving}
        >
          <Ionicons name="lock-closed-outline" size={20} color={colors.blue} />
          <Text style={styles.passwordButtonText}>Trocar senha</Text>
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
  passwordBox: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 56,
    borderWidth: 1.5,
    borderColor: colors.border,
    borderRadius: 14,
    paddingHorizontal: 16,
    backgroundColor: colors.white,
    marginBottom: 8,
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
  passwordButton: {
    height: 56,
    borderRadius: 28,
    borderWidth: 1.5,
    borderColor: colors.blue,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    marginTop: 12,
  },
  passwordButtonText: { color: colors.blue, fontSize: 18, fontWeight: '600' },
});