import { Platform } from 'react-native';
import * as Notifications from 'expo-notifications';
import { listSchedules } from './scheduleService';
import { listOccurrences } from './occurrenceService';
import { formatTime } from '../utils/format';

// Quantas horas à frente o app agenda (o iPhone aceita no máximo 64 avisos agendados)
const HORIZON_HOURS = 48;
const MAX_NOTIFICATIONS = 60;

const MINUTE_MS = 60 * 1000;
const HOUR_MS = 60 * MINUTE_MS;
const DAY_MS = 24 * HOUR_MS;

// Quanto vale cada unidade fixa, em milissegundos
const UNIT_MS = {
  MINUTE: MINUTE_MS,
  HOUR: HOUR_MS,
  DAY: DAY_MS,
  WEEK: 7 * DAY_MS,
};

// Textos do aviso, conforme o tipo do agendamento
const KIND_LABELS = {
  MEDICATION: 'Remédio',
  CONSULTATION: 'Consulta',
  EXAM: 'Exame',
};

// Canais do Android (depois de criados, não dá para trocar som e vibração)
const CHANNEL_ALARM = 'alarme';
const CHANNEL_SILENT = 'lembrete-silencioso';

// Quando o aviso chega com o app aberto: mostra na tela e toca só se o alarme estiver ligado
Notifications.setNotificationHandler({
  handleNotification: async (notification) => ({
    shouldShowBanner: true,
    shouldShowList: true,
    shouldPlaySound: !!notification.request.content.data?.alarm,
    shouldSetBadge: false,
  }),
});

async function setupAndroidChannels() {
  if (Platform.OS !== 'android') return;

  await Notifications.setNotificationChannelAsync(CHANNEL_ALARM, {
    name: 'Alarme dos remédios e consultas',
    importance: Notifications.AndroidImportance.MAX,
    vibrationPattern: [0, 400, 250, 400, 250, 400],
    sound: 'default',
  });
  await Notifications.setNotificationChannelAsync(CHANNEL_SILENT, {
    name: 'Lembretes silenciosos',
    importance: Notifications.AndroidImportance.DEFAULT,
    sound: null,
    vibrationPattern: [0],
  });
}

// Pede permissão para avisar (só pergunta se ainda for possível)
async function ensurePermission() {
  const current = await Notifications.getPermissionsAsync();
  if (current.granted) return true;
  if (!current.canAskAgain) return false;

  const asked = await Notifications.requestPermissionsAsync();
  return asked.granted;
}

// "10 minutos", "1 hora", "2 dias"...
function leadText(value, unit) {
  const names = {
    MINUTE: ['minuto', 'minutos'],
    HOUR: ['hora', 'horas'],
    DAY: ['dia', 'dias'],
    WEEK: ['semana', 'semanas'],
  };
  const [one, many] = names[unit] ?? ['minuto', 'minutos'];
  return `${value} ${value === 1 ? one : many}`;
}

// Quanto tempo antes do horário o aviso deve chegar (0 = na hora)
function leadMs(schedule) {
  if (!schedule.reminderValue || !UNIT_MS[schedule.reminderUnit]) return 0;
  return schedule.reminderValue * UNIT_MS[schedule.reminderUnit];
}

// Calcula os horários de um agendamento entre "from" e "until".
// O back-end guarda só o início e o intervalo: os horários são calculados aqui.
function occurrenceTimes(schedule, from, until) {
  const start = new Date(schedule.startAt).getTime();
  const end = schedule.endAt ? new Date(schedule.endAt).getTime() : Infinity;
  const limit = Math.min(until, end);
  const value = schedule.intervalValue;
  const unit = schedule.intervalUnit;
  const times = [];

  // Aparece uma vez só
  if (unit === 'UNIQUE' || !value || value <= 0) {
    if (start >= from && start <= limit) times.push(start);
    return times;
  }

  // Intervalos fixos (minutos, horas, dias, semanas): conta por multiplicação
  if (UNIT_MS[unit]) {
    const step = UNIT_MS[unit] * value;
    let n = Math.max(0, Math.ceil((from - start) / step));
    while (times.length < 200) {
      const t = start + n * step;
      if (t > limit) break;
      times.push(t);
      n++;
    }
    return times;
  }

  // Meses e anos: o tamanho varia, então soma no calendário
  const monthsPerStep = unit === 'YEAR' ? 12 * value : value;
  for (let k = 0; k < 1000; k++) {
    const d = new Date(start);
    d.setMonth(d.getMonth() + k * monthsPerStep);
    const t = d.getTime();
    if (t > limit) break;
    if (t >= from) times.push(t);
  }
  return times;
}

// Monta o texto do aviso
function buildContent(schedule, time) {
  const lead = leadMs(schedule);
  const kind = KIND_LABELS[schedule.type] ?? 'Compromisso';
  const clock = formatTime(new Date(time).toISOString());

  return {
    title: lead
      ? `Daqui a ${leadText(schedule.reminderValue, schedule.reminderUnit)}`
      : 'Está na hora',
    body: `${kind}: ${schedule.title} (${clock})`,
    sound: schedule.alarmEnabled ? 'default' : false,
    data: { alarm: !!schedule.alarmEnabled, scheduleId: schedule.id },
  };
}

async function runSync(profileId) {
  if (!profileId) return 0;

  await setupAndroidChannels();
  if (!(await ensurePermission())) return 0;

  const now = Date.now();
  const [schedules, occurrences] = await Promise.all([
    listSchedules(profileId),
    listOccurrences(profileId),
  ]);

  // Quem já foi confirmado não precisa de aviso
  const confirmed = new Set(
    occurrences
      .filter((o) => o.status === 'CONFIRMED')
      .map((o) => `${o.scheduleId}-${new Date(o.scheduledAt).getTime()}`)
  );

  const planned = [];
  for (const schedule of schedules) {
    if (schedule.active === false || !schedule.notifyEnabled) continue;

    const lead = leadMs(schedule);
    const times = occurrenceTimes(
      schedule,
      now,
      now + HORIZON_HOURS * HOUR_MS + lead
    );

    for (const time of times) {
      if (confirmed.has(`${schedule.id}-${time}`)) continue;
      const triggerAt = time - lead;
      if (triggerAt <= now + 5000) continue; // já passou (ou é agora)
      planned.push({ schedule, time, triggerAt });
    }
  }

  // Os mais próximos primeiro, respeitando o limite do celular
  planned.sort((a, b) => a.triggerAt - b.triggerAt);
  const chosen = planned.slice(0, MAX_NOTIFICATIONS);

  // Apaga tudo e agenda de novo: assim editar ou deletar nunca deixa aviso velho
  await Notifications.cancelAllScheduledNotificationsAsync();

  for (const item of chosen) {
    await Notifications.scheduleNotificationAsync({
      content: buildContent(item.schedule, item.time),
      trigger: {
        type: Notifications.SchedulableTriggerInputTypes.DATE,
        date: new Date(item.triggerAt),
        channelId: item.schedule.alarmEnabled ? CHANNEL_ALARM : CHANNEL_SILENT,
      },
    });
  }
  return chosen.length;
}

// Fila: se duas sincronizações forem pedidas juntas, uma espera a outra terminar
// (senão os avisos poderiam ser agendados em dobro).
let queue = Promise.resolve(0);

// Atualiza os avisos do celular com o que existe no back-end.
// Nunca "quebra" a tela: se algo falhar, devolve 0 e segue.
export function syncNotifications(profileId) {
  queue = queue.then(() => runSync(profileId)).catch(() => 0);
  return queue;
}

// Apaga todos os avisos agendados (usar ao sair da conta)
export async function clearNotifications() {
  try {
    await Notifications.cancelAllScheduledNotificationsAsync();
  } catch (e) {
    // sem problema: na próxima sincronização tudo é refeito
  }
}