import { request } from './api';

export async function listRestrictions(profileId) {
  const { data } = await request(`/restriction?profileId=${profileId}`);
  return data;
}

export async function createRestriction(body) {
  const { data } = await request('/restriction', { method: 'POST', body });
  return data;
}

// O id vai na URL; o corpo leva profileId, type, severity, title, note e active
export async function updateRestriction(id, body) {
  const { data } = await request(`/restriction/${id}`, { method: 'PUT', body });
  return data;
}