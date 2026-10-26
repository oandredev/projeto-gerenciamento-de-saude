import { request, setToken } from './api';

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
  return data;
}