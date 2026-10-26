// Regras de senha: as mesmas que o back-end exige.
export function getPasswordRules(password) {
  return [
    {
      id: 'length',
      label: 'Pelo menos 8 caracteres',
      ok: password.length >= 8,
    },
    {
      id: 'lower',
      label: 'Uma letra minúscula',
      ok: password !== password.toUpperCase(),
    },
    {
      id: 'upper',
      label: 'Uma letra maiúscula',
      ok: password !== password.toLowerCase(),
    },
    {
      id: 'digit',
      label: 'Um número',
      ok: /\d/.test(password),
    },
    {
      id: 'special',
      label: 'Um símbolo (! @ # . ...)',
      ok: /[!-\/:-@\[-`{-~]/.test(password),
    },
  ];
}