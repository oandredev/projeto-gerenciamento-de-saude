import { StatusBar } from 'expo-status-bar';
import LoginScreen from './src/screens/LoginScreen';

export default function App() {
  return (
    <>
      <LoginScreen />
      <StatusBar style="light" />
    </>
  );
}