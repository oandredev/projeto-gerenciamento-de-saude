import { request } from './api';

// Lista as ocorrências do perfil (GET /occurrence).
// "reference" é a hora atual do celular em UTC: o back-end confere com o relógio dele.
export async function listOccurrences(profileId, { type, order = 'ASC' } = {}) {
  let query =
    `profileId=${profileId}` +
    `&reference=${encodeURIComponent(new Date().toISOString())}` +
    `&order=${order}`;
  if (type) {
    query += `&type=${type}`;
  }
  const { data } = await request(`/occurrence?${query}`);
  return data;
}

// "Já tomei" / "Realizado" (POST /confirmation)
export async function confirmOccurrence({ scheduleId, scheduledAt }) {
  const { data } = await request('/confirmation', {
    method: 'POST',
    body: { scheduleId, scheduleAt: scheduledAt },
  });
  return data;
}