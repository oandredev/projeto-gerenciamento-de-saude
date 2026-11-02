// Traduz as mensagens de erro do back-end (em inglês) para português.

const EXACT = {
  'Email or password are invalid': 'E-mail ou senha incorretos.',
  'Email already in use': 'Este e-mail já está cadastrado.',
  'User not found': 'Usuário não encontrado.',
  'This account was archived': 'Esta conta foi arquivada.',

  'Email is required': 'Digite o e-mail.',
  'Email format is invalid': 'O e-mail digitado não é válido.',
  'Name is required': 'Digite o nome.',
  'Name must contain only letters': 'O nome deve ter apenas letras.',
  'Password is required': 'Digite a senha.',
  'Password is too long': 'A senha é muito longa.',
  'Password must contain lowercase, uppercase, number and special character':
    'A senha precisa ter letra minúscula, maiúscula, número e símbolo.',
  'Password contains invalid characters (emojis and spaces are not allowed)':
    'A senha tem caracteres inválidos (não use emojis nem espaços).',
  'Remember is required': 'Erro interno no login. Tente novamente.',

  'Avatar ID is invalid': 'Avatar inválido.',
  'Profile ID is invalid': 'Perfil inválido.',
  'Profile not found': 'Perfil não encontrado.',
  'This profile name is already being used by this same user':
    'Você já tem um perfil com esse nome.',

  'Request body is missing or malformed':
    'Os dados enviados estão incompletos. Tente novamente.',

  'Title is required': 'Digite o nome.',
  'Title contains invalid characters': 'O nome tem caracteres que não são aceitos.',
  'Medicine name is required': 'Digite o nome do remédio.',
  'Medicine name is invalid': 'O nome do remédio tem caracteres que não são aceitos.',
  'Dosage amount is invalid': 'A dose informada não é válida.',
  'Dosage unit is required': 'Escolha a unidade da dose.',
  'Interval value must be greater than zero': 'O intervalo precisa ser maior que zero.',

  'Type is required': 'Escolha o tipo.',
  'Severity is required': 'Escolha a gravidade.',
  'Restriction not found': 'Restrição não encontrada.',
  'Active schedule not found': 'Este item não existe mais. Volte e atualize a lista.',
};

const PATTERNS = [
  [/^Password must have at least (\d+) characters$/, (m) => `A senha precisa ter pelo menos ${m[1]} caracteres.`],
  [/^Name must have between (\d+) and (\d+) characters$/, (m) => `O nome deve ter entre ${m[1]} e ${m[2]} caracteres.`],
  [/^Email must have between (\d+) and (\d+) characters$/, (m) => `O e-mail deve ter entre ${m[1]} e ${m[2]} caracteres.`],
  [/^Title must have between (\d+) and (\d+) characters$/, (m) => `O nome deve ter entre ${m[1]} e ${m[2]} caracteres.`],
  [/^(.+) must have at most (\d+) characters$/, (m) => `Este campo aceita no máximo ${m[2]} caracteres.`],
];