import { request } from './api';
import { syncNotifications } from './notificationService';

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
  syncNotifications(); // atualiza os avisos do celular (em segundo plano)
  return data;
}

// PUT /schedule: o id vai no corpo, junto com o type (que não pode mudar)
export async function updateSchedule(body) {
  const { data } = await request('/schedule', { method: 'PUT', body });
  syncNotifications();
  return data;
}

// Deleta o agendamento (DELETE /schedule/{id}).
// No back-end é uma exclusão lógica: ele só marca como inativo,
// e as confirmações já feitas continuam guardadas como histórico.
export async function deleteSchedule(id) {
  await request(`/schedule/${id}`, { method: 'DELETE' });
  syncNotifications();
}