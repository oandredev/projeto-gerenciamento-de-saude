import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { StyleSheet, Text, View } from 'react-native';
import LoginScreen from '../screens/LoginScreen';
import SignUpScreen from '../screens/SignUpScreen';
import HomeScreen from '../screens/HomeScreen';
import { colors } from '../styles/theme';
import MedicationsScreen from '../screens/MedicationsScreen';

const Stack = createNativeStackNavigator();
const Tab = createBottomTabNavigator();

function PlaceholderScreen({ title }) {
    return (
        <View style={styles.placeholder}>
            <Text style={styles.placeholderText}>{title}</Text>
        </View>
    );
}

function AppointmentsScreen() {
    return <PlaceholderScreen title="Consultas" />;
}

function MoreScreen() {
    return <PlaceholderScreen title="Mais" />;
}
function TabIcon({ name, color, focused }) { // eu quase esqueci da barra laranja em cima do ícone selecionado 
    return (
        <View style={styles.tabIconWrap}>
            {focused && <View style={styles.tabIndicator} />}
            <MaterialCommunityIcons name={name} size={28} color={color} />
        </View>
    );
}
function MainTabs() {
    return (
        <Tab.Navigator
            screenOptions={{
                headerShown: false,
                tabBarActiveTintColor: colors.blue,
                tabBarInactiveTintColor: colors.gray,
                tabBarLabelStyle: { fontSize: 14 },
                tabBarStyle: { height: 76, paddingTop: 8, paddingBottom: 10 },
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
                <Stack.Screen name="MainTabs" component={MainTabs} />
            </Stack.Navigator>
        </NavigationContainer>
    );
}

const styles = StyleSheet.create({
    placeholder: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: colors.white,
    },
    placeholderText: {
        color: colors.blue,
        fontSize: 28,
        fontWeight: 'bold'
    },
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