// Os valores (MEDICATION, FOOD...) são os do back-end; o texto é o que a pessoa vê.
export const RESTRICTION_TYPES = [
  { value: 'MEDICATION', label: 'Remédio', icon: 'pill' },
  { value: 'FOOD', label: 'Alimento', icon: 'food-apple-outline' },
  { value: 'SUBSTANCE', label: 'Substância', icon: 'flask-outline' },
  { value: 'OTHER', label: 'Outro', icon: 'alert-circle-outline' },
];

// "SEVERY" é assim mesmo no back-end (com esse erro de digitação). Na tela mostramos "Grave".
export const RESTRICTION_SEVERITIES = [
  { value: 'MILD', label: 'Leve' },
  { value: 'MODERATE', label: 'Moderada' },
  { value: 'SEVERY', label: 'Grave' },
];