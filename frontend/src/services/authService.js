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

// O PUT /user substitui tudo e exige a senha (ele regrava a senha a cada edição)
export async function updateAccount({ name, email, password }) {
  const { data } = await request('/user', {
    method: 'PUT',
    body: { name, email, password, active: true },
  });
  currentUser = data;
  return data;
}