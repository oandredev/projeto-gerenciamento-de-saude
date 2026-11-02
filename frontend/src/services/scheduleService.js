import { request } from './api';

// Lista os agendamentos do perfil (GET /schedule).
// type: 'MEDICATION', 'CONSULTATION' ou 'EXAM' (sem type, vem tudo)
export async function listSchedules(profileId, { type, order = 'ASC' } = {}) {
  let query = `profileId=${profileId}&order=${order}`;
  if (type) {
    query += `&type=${type}`;
  }
  const { data } = await request(`/schedule?${query}`);
  return data;
}

// Cria um agendamento (POST /schedule)
export async function createSchedule(body) {
  const { data } = await request('/schedule', { method: 'POST', body });
  return data;
}

// PUT /schedule: o id vai no corpo, junto com o type (que não pode mudar)
export async function updateSchedule(body) {
  const { data } = await request('/schedule', { method: 'PUT', body });
  return data;
}