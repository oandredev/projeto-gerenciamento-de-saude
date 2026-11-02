import { request } from './api';

// Perfil que está aberto no momento (fica na memória, como o token)
let activeProfile = null;

export function setActiveProfile(profile) {
  activeProfile = profile;
}

export function getActiveProfile() {
  return activeProfile;
}

// Lista os perfis da conta que está logada (GET /profile)
export async function listProfiles() {
  const { data } = await request('/profile');
  return data;
}

// Cria um perfil novo (POST /profile)
export async function createProfile({ name, avatarId = 1 }) {
  const { data } = await request('/profile', {
    method: 'POST',
    body: { name, avatarId },
  });
  return data;
}

// O id vai na URL; o corpo leva name, avatarId e active (tudo obrigatório)
export async function updateProfile(id, body) {
  const { data } = await request(`/profile/${id}`, { method: 'PUT', body });
  return data;
}
