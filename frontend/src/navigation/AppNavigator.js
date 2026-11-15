import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Platform, StyleSheet, View } from 'react-native';
import LoginScreen from '../screens/LoginScreen';
import SignUpScreen from '../screens/SignUpScreen';
import HomeScreen from '../screens/HomeScreen';
import { colors } from '../styles/theme';
import MedicationsScreen from '../screens/MedicationsScreen';
import AppointmentsScreen from '../screens/AppointmentsScreen';
import ProfileSelectScreen from '../screens/ProfileSelectScreen';
import MedicationFormScreen from '../screens/MedicationFormScreen';
import AppointmentFormScreen from '../screens/AppointmentFormScreen';
import MoreScreen from '../screens/MoreScreen';
import RestrictionsScreen from '../screens/RestrictionsScreen';
import RestrictionFormScreen from '../screens/RestrictionFormScreen';
import ProfileEditScreen from '../screens/ProfileEditScreen';
import AccountEditScreen from '../screens/AccountEditScreen';
import ChangePasswordScreen from '../screens/ChangePasswordScreen';
import UpcomingNotificationsScreen from '../screens/UpcomingNotificationsScreen';

const Stack = createNativeStackNavigator();
const Tab = createBottomTabNavigator();

function TabIcon({ name, color, focused }) {
    return (
        <View style={styles.tabIconWrap}>
            {focused && <View style={styles.tabIndicator} />}
            <MaterialCommunityIcons name={name} size={28} color={color} />
        </View>
    );
}
function MainTabs() {
    // No Android, a barra de navegação do celular (botões ▮▮▮ ○ ‹) fica por cima do app.
    // Somamos o tamanho dela à barra de baixo para as abas não ficarem escondidas.
    const insets = useSafeAreaInsets();
    const bottomInset = Platform.OS === 'android' ? insets.bottom : 0;

    return (
        <Tab.Navigator
            screenOptions={{
                headerShown: false,
                animation: 'shift',
                tabBarActiveTintColor: colors.blue,
                tabBarInactiveTintColor: colors.gray,
                tabBarLabelStyle: { fontSize: 14 },
                tabBarStyle: {
                    height: 76 + bottomInset,
                    paddingTop: 8,
                    paddingBottom: 10 + bottomInset,
                },
            }}
        >
            <Tab.Screen
                name="Home"
                component={HomeScreen}
                options={{
                    tabBarLabel: 'Hoje',
                    tabBarIcon: ({ color, focused }) => (
                        <TabIcon name="home-outline" color={color} focused={focused} />
                    ),
                }}
            />
            <Tab.Screen
                name="Medications"
                component={MedicationsScreen}
                options={{
                    tabBarLabel: 'Remédios',
                    tabBarIcon: ({ color, focused }) => (
                        <TabIcon name="pill" color={color} focused={focused} />
                    ),
                }}
            />
            <Tab.Screen
                name="Appointments"
                component={AppointmentsScreen}
                options={{
                    tabBarLabel: 'Consultas',
                    tabBarIcon: ({ color, focused }) => (
                        <TabIcon name="stethoscope" color={color} focused={focused} />
                    ),
                }}
            />
            <Tab.Screen
                name="More"
                component={MoreScreen}
                options={{
                    tabBarLabel: 'Mais',
                    tabBarIcon: ({ color, focused }) => (
                        <TabIcon name="menu" color={color} focused={focused} />
                    ),
                }}
            />
        </Tab.Navigator>
    );
}

export default function AppNavigator() {
    return (
        <NavigationContainer>
            <Stack.Navigator screenOptions={{ headerShown: false }}>
                <Stack.Screen name="Login" component={LoginScreen} />
                <Stack.Screen name="SignUp" component={SignUpScreen} />
                <Stack.Screen name="ProfileSelect" component={ProfileSelectScreen} />
                <Stack.Screen name="MainTabs" component={MainTabs} />
                <Stack.Screen name="MedicationForm" component={MedicationFormScreen} />
                <Stack.Screen name="AppointmentForm" component={AppointmentFormScreen} />
                <Stack.Screen name="Restrictions" component={RestrictionsScreen} />
                <Stack.Screen name="RestrictionForm" component={RestrictionFormScreen} />
                <Stack.Screen name="ProfileEdit" component={ProfileEditScreen} />
                <Stack.Screen name="AccountEdit" component={AccountEditScreen} />
                <Stack.Screen name="ChangePassword" component={ChangePasswordScreen} />
                <Stack.Screen
                    name="UpcomingNotifications"
                    component={UpcomingNotificationsScreen}
                />
            </Stack.Navigator>
        </NavigationContainer>
    );
}

const styles = StyleSheet.create({
    tabIconWrap: { alignItems: 'center' },
    tabIndicator: {
        position: 'absolute',
        top: -10,
        width: 36,
        height: 4,
        borderRadius: 2,
        backgroundColor: colors.orange,
    },
});