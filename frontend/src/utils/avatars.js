// O back-end guarda só o número do avatar. Aqui cada número vira uma cor.
export const AVATARS = [
  { id: 1, bg: '#F57C00' },
  { id: 2, bg: '#2E7D32' },
  { id: 3, bg: '#8E24AA' },
  { id: 4, bg: '#00838F' },
  { id: 5, bg: '#C62828' },
  { id: 6, bg: '#5D4037' },
];

export function getAvatar(id) {
  return AVATARS.find((a) => a.id === id) ?? AVATARS[0];
}