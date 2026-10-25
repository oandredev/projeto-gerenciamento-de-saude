import { StyleSheet, Text, View } from 'react-native';
import { colors } from '../styles/theme';

export default function HomeScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Hoje</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: colors.white,
  },
  title: { color: colors.blue, fontSize: 28, fontWeight: 'bold' },
});