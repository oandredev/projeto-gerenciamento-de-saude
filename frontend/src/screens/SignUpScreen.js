import { StyleSheet, View } from 'react-native';
import Header from '../components/Header';
import { colors } from '../styles/theme';

export default function SignUpScreen({ navigation }) {
  return (
    <View style={styles.container}>
      <Header
        title="Cadastro"
        showProfileSwitch={false}
        onBack={() => navigation.goBack()}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: colors.white },
});