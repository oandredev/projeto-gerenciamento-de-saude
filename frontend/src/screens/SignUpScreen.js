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
import { signup, login } from '../services/authService';
import PasswordRules from '../components/PasswordRules';
import { getPasswordRules } from '../utils/validation';

// Campo de senha com o olhinho. Cada campo guarda sozinho se está mostrando a senha.
function PasswordField({ placeholder, value, onChangeText }) {
    const [visible, setVisible] = useState(false);

    return (
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
            <Pressable style={styles.eyeButton} onPress={() => setVisible(!visible)}>
                <Ionicons
                    name={visible ? 'eye-off-outline' : 'eye-outline'}
                    size={24}
                    color={colors.gray}
                />
            </Pressable>
        </View>
    );
}

export default function SignUpScreen({ navigation }) {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    async function handleSignUp() {
        if (!name || !email || !password || !confirmPassword) {
            setError('Preencha todos os campos.');
            return;
        }
        if (getPasswordRules(password).some((rule) => !rule.ok)) {
            setError('A senha ainda não cumpre todas as regras.');
            return;
        }
        if (password !== confirmPassword) {
            setError('As senhas não são iguais.');
            return;
        }
        setError('');
        setLoading(true);
        try {
            await signup({ name, email, password });
            await login({ email, password });
            navigation.replace('ProfileSelect');
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }

    return (
        <KeyboardAvoidingView
            style={styles.container}
            behavior={Platform.OS === 'ios' ? 'padding' : undefined}
        >
            <Header
                title="Cadastro"
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
                    placeholder="Digite seu nome"
                    placeholderTextColor={colors.placeholder}
                    value={name}
                    onChangeText={setName}
                />

                <Text style={styles.label}>E-mail</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Digite seu e-mail"
                    placeholderTextColor={colors.placeholder}
                    keyboardType="email-address"
                    autoCapitalize="none"
                    autoCorrect={false}
                    value={email}
                    onChangeText={setEmail}
                />

                <Text style={styles.label}>Senha</Text>
                <PasswordField
                    placeholder="Crie uma senha"
                    value={password}
                    onChangeText={setPassword}
                />

                <PasswordRules password={password} />

                <Text style={styles.label}>Confirmar senha</Text>
                <PasswordField
                    placeholder="Digite a senha de novo"
                    value={confirmPassword}
                    onChangeText={setConfirmPassword}
                />

                {error !== '' && <Text style={styles.error}>{error}</Text>}

                <Pressable
                    style={styles.primaryButton}
                    onPress={handleSignUp}
                    disabled={loading}
                >
                    <Text style={styles.primaryText}>
                        {loading ? 'Criando...' : 'Criar conta'}
                    </Text>
                    {!loading && (
                        <Ionicons name="checkmark-circle-outline" size={24} color={colors.orangeLight} />
                    )}
                </Pressable>

                <Pressable
                    style={styles.link}
                    onPress={() => navigation.goBack()}
                >
                    <Text style={styles.linkText}>Já tenho conta.</Text>
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
        marginBottom: 20,
    },
    passwordInput: {
        flex: 1,
        fontSize: 18,
        color: colors.ink,
    },
    eyeButton: { paddingLeft: 12, paddingVertical: 8 },
    error: {
        color: colors.orangeText,
        fontSize: 16,
        fontWeight: '600',
        marginBottom: 12,
    },
    primaryButton: {
        height: 56,
        borderRadius: 28,
        backgroundColor: colors.blue,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 8,
        marginTop: 8,
    },
    primaryText: { color: colors.white, fontSize: 20, fontWeight: '600' },
    link: { alignItems: 'center', paddingVertical: 20 },
    linkText: { color: colors.blue, fontSize: 18, fontWeight: '600' },
});