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
import Logo from '../components/Logo';
import { colors } from '../styles/theme';

export default function LoginScreen() {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    function handleLogin() {
        console.log('Entrar:', login);
    }

    return (
        <KeyboardAvoidingView
            style={styles.container}
            behavior={Platform.OS === 'ios' ? 'padding' : undefined}
        >
            <View style={styles.header}>
                <Logo size={44} />
                <View style={styles.headerText}>
                    <Text style={styles.brand}>
                        A<Text style={styles.brandAmp}>&</Text>F
                    </Text>
                    <Text style={styles.brandSub}>Health Management</Text>
                </View>
            </View>
            <View style={styles.headerLine} />

            <ScrollView
                contentContainerStyle={styles.scroll}
                keyboardShouldPersistTaps="handled"
            >
                <View style={styles.help}>
                    <Ionicons name="help-circle-outline" size={32} color={colors.blue} />
                    <View style={styles.helpTextBox}>
                        <Text style={styles.helpText}>
                            Dúvidas no acesso? Veja o passo a passo.
                        </Text>
                        <Text style={styles.helpLink}>Acessar manual.</Text>
                    </View>
                </View>

                <Text style={styles.label}>E-mail</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Digite seu e-mail"
                    placeholderTextColor={colors.placeholder}
                    keyboardType="email-address"
                    autoCapitalize="none"
                    value={login}
                    onChangeText={setLogin}
                />

                <View style={styles.passwordRow}>
                    <Text style={styles.label}>Senha</Text>
                    <Text style={styles.forgot}>Esqueci minha senha</Text>
                </View>
                <TextInput
                    style={styles.input}
                    placeholder="Digite sua senha"
                    placeholderTextColor={colors.placeholder}
                    secureTextEntry
                    value={password}
                    onChangeText={setPassword}
                />

                <Pressable style={styles.primaryButton}>
                    <Text style={styles.primaryText}>Entrar com biometria</Text>
                    <Ionicons name="finger-print" size={22} color={colors.orangeLight} />
                </Pressable>

                <Pressable style={styles.outlineBlue} onPress={handleLogin}>
                    <Text style={styles.outlineBlueText}>Entrar</Text>
                </Pressable>

                <Text style={styles.register}>Ainda não é cadastrado?</Text>
                <Pressable style={styles.outlineOrange}>
                    <Text style={styles.outlineOrangeText}>Fazer cadastro</Text>
                </Pressable>
            </ScrollView>
        </KeyboardAvoidingView>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, backgroundColor: colors.white },
    header: {
        backgroundColor: colors.blue,
        flexDirection: 'row',
        alignItems: 'center',
        paddingTop: 56,
        paddingBottom: 20,
        paddingHorizontal: 24,
    },
    headerText: { marginLeft: 14 },
    brand: { color: colors.white, fontSize: 24, fontWeight: 'bold' },
    brandAmp: { color: colors.orange },
    brandSub: { color: colors.white, fontSize: 14 },
    headerLine: { height: 3, backgroundColor: colors.orange },
    scroll: { padding: 24 },
    help: {
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: colors.blueBg,
        borderRadius: 16,
        padding: 16,
        marginBottom: 24,
    },
    helpTextBox: { flex: 1, marginLeft: 12 },
    helpText: { color: colors.ink, fontSize: 15 },
    helpLink: {
        color: colors.blue,
        fontSize: 15,
        textDecorationLine: 'underline',
        marginTop: 2,
    },
    label: {
        color: colors.blue,
        fontSize: 15,
        fontWeight: '600',
        marginBottom: 8,
    },
    passwordRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'baseline',
    },
    forgot: {
        color: colors.blue,
        fontSize: 15,
        textDecorationLine: 'underline',
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
    primaryText: { color: colors.white, fontSize: 18, fontWeight: '600' },
    outlineBlue: {
        height: 56,
        borderRadius: 28,
        borderWidth: 1.5,
        borderColor: colors.blue,
        alignItems: 'center',
        justifyContent: 'center',
        marginTop: 12,
    },
    outlineBlueText: { color: colors.blue, fontSize: 18, fontWeight: '600' },
    register: { color: colors.gray, fontSize: 16, marginTop: 28, marginBottom: 12 },
    outlineOrange: {
        height: 56,
        borderRadius: 28,
        borderWidth: 1.5,
        borderColor: colors.orange,
        alignItems: 'center',
        justifyContent: 'center',
    },
    outlineOrangeText: {
        color: colors.orangeText,
        fontSize: 18,
        fontWeight: '600',
    },
});