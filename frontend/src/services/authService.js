import { request, setToken } from './api';

// Dados da conta que entrou (ficam só na memória do app, como o token)
let currentUser = null;

export function getCurrentUser() {
  return currentUser;
}

export function clearCurrentUser() {
  currentUser = null;
}

export async function signup({ name, email, password }) {
  const { data } = await request('/user/signup', {
    method: 'POST',
    body: { name, email, password },
  });
  return data;
}

export async function login({ email, password, remember = false }) {
  const { data, headers } = await request('/user/login', {
    method: 'POST',
    body: { email, password, remember },
  });

  const authorization = headers.get('authorization');
  if (authorization) {
    setToken(authorization.replace('Bearer ', ''));
  }
  currentUser = data;
  return data;
}

// Altera nome e e-mail (PUT /user).
// A senha atual só é enviada quando o e-mail muda: é o que o back-end exige.
export async function updateAccount({ name, email, currentPassword }) {
  const body = { name, email };
  if (currentPassword) {
    body.currentPassword = currentPassword;
  }

  const { data } = await request('/user', { method: 'PUT', body });
  currentUser = data;
  return data;
}

// Troca a senha (PUT /user/password): precisa da senha atual e da nova.
export async function changePassword({ currentPassword, newPassword }) {
  const { data } = await request('/user/password', {
    method: 'PUT',
    body: { currentPassword, newPassword },
  });
  currentUser = data;
  return data;
}