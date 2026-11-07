# Abas (equivalente ao `.tabs` do HTML vanilla)

O arquivo do navegador (onde fica o `createBottomTabNavigator`) não foi enviado.
Nele, adicione `tabBarAccessibilityLabel` em cada `Tab.Screen`:

```js
<Tab.Screen
  name="Home"
  component={HomeScreen}
  options={{
    tabBarLabel: 'Início',
    tabBarAccessibilityLabel: 'Ir para a tela de início',
  }}
/>
<Tab.Screen
  name="Medications"
  component={MedicationsScreen}
  options={{
    tabBarLabel: 'Remédios',
    tabBarAccessibilityLabel: 'Ir para a lista de remédios',
  }}
/>
<Tab.Screen
  name="Appointments"
  component={AppointmentsScreen}
  options={{
    tabBarLabel: 'Consultas',
    tabBarAccessibilityLabel: 'Ir para a lista de consultas',
  }}
/>
```
