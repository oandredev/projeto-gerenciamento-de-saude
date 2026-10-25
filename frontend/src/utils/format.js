const WEEKDAYS = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'];
const MONTHS = [
  'jan', 'fev', 'mar', 'abr', 'mai', 'jun',
  'jul', 'ago', 'set', 'out', 'nov', 'dez',
];

const pad = (n) => String(n).padStart(2, '0');

export function formatTime(iso) {
  const d = new Date(iso);
  return `${pad(d.getHours())}:${pad(d.getMinutes())}`;
}

export function formatDate(iso) {
  const d = new Date(iso);
  return `${WEEKDAYS[d.getDay()]}, ${d.getDate()} ${MONTHS[d.getMonth()]}`;
}

// Calcula os horários do dia a partir do início e do intervalo.
// Ex.: início 08:00, a cada 12 horas -> ['08:00', '20:00']
export function getDailyTimes(startAt, intervalValue, intervalUnit) {
  const start = new Date(startAt);
  if (intervalUnit === 'HOUR' && intervalValue > 0 && intervalValue < 24) {
    const perDay = Math.floor(24 / intervalValue);
    const times = [];
    for (let i = 0; i < perDay; i++) {
      const d = new Date(start.getTime() + i * intervalValue * 3600000);
      times.push(formatTime(d.toISOString()));
    }
    return times.sort();
  }
  return [formatTime(startAt)];
}

export function joinTimes(times) {
  if (times.length <= 2) return times.join(' e ');
  return `${times.slice(0, -1).join(', ')} e ${times[times.length - 1]}`;
}

export const STRIPE_LABELS = {
  NONE: 'Sem tarja',
  YELLOW: 'Tarja amarela',
  RED: 'Tarja vermelha',
  BLACK: 'Tarja preta',
  OTHER: 'Outra tarja',
};