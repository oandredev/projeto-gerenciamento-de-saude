<p align="right"><b>Português</b> | <a href="README.md">English</a></p>

# Health Management - Backend

API REST do projeto acadêmico **Gerenciamento de Saúde**, responsável por gerenciar usuários, perfis, restrições, agendamentos (remédios, consultas e exames), ocorrências e confirmações.

## Responsável

- **André Rodrigues** - [github.com/oandredev](https://github.com/oandredev)

## Tecnologias e dependências

| Tecnologia           | Versão / Artefato                    | Uso                           |
| -------------------- | ------------------------------------ | ----------------------------- |
| Java                 | 17                                   | Linguagem                     |
| Spring Boot          | 4.1.1 (`spring-boot-starter-parent`) | Framework base                |
| Spring Web MVC       | `spring-boot-starter-webmvc`         | Camada REST                   |
| Spring Data JPA      | `spring-boot-starter-data-jpa`       | Persistência (Hibernate)      |
| H2 Database          | `com.h2database:h2`                  | Banco de dados em arquivo     |
| H2 Console           | `spring-boot-h2console`              | Interface web do banco        |
| Spring Security      | `spring-boot-starter-security`       | Autenticação e autorização    |
| JWT (jjwt)           | 0.12.6 (`api`, `impl`, `jackson`)    | Geração e validação de tokens |
| Springdoc OpenAPI    | 3.2.0                                | Documentação (Swagger UI)     |
| Spring REST Client   | `spring-boot-starter-restclient`     | Cliente HTTP                  |
| Spring Boot DevTools | runtime / opcional                   | Reload em desenvolvimento     |

Dependências de teste: `spring-boot-starter-data-jpa-test`, `spring-boot-starter-restclient-test` e `spring-boot-starter-webmvc-test`.

## Pré-requisitos

- JDK 17 ou superior
- Maven 3.9+ (ou o wrapper `mvnw` do projeto)

## Configuração

### 1. Arquivo `.env`

Crie um arquivo `.env` na raiz do módulo do backend (mesmo nível do `pom.xml`):

```properties
DATABASE_NAME=
DB_USERNAME=
DB_PASSWORD=

SPRING_USERNAME=
SPRING_PASSWORD=

JWT_KEY=

# 3600000ms = 1 HOUR | 2592000000ms = 30 DAYS
JWT_EXPIRATION_MS=3600000
JWT_REMEMBER_EXPIRATION_MS=2592000000

SERVER_PORT=8080
```

| Variável                              | Descrição                                                 |
| ------------------------------------- | --------------------------------------------------------- |
| `DATABASE_NAME`                       | Nome do arquivo do banco H2 (gerado em `./data/`)         |
| `DB_USERNAME` / `DB_PASSWORD`         | Credenciais do banco de dados                             |
| `SPRING_USERNAME` / `SPRING_PASSWORD` | Usuário e senha para o Spring Security                    |
| `JWT_KEY`                             | Chave secreta usada para assinar os tokens JWT (32 bytes) |
| `JWT_EXPIRATION_MS`                   | Duração do token padrão (em milissegundos)                |
| `JWT_REMEMBER_EXPIRATION_MS`          | Duração do token quando o login usa "lembrar de mim"      |
| `SERVER_PORT`                         | Porta da aplicação. Padrão: 8080                          |

> O arquivo `.env` contém dados sensíveis e **não deve ser versionado**. Adicione-o ao `.gitignore`.

### 2. `application.properties`

```properties
spring.application.name=health-management

# .env
spring.config.import=optional:file:.env[.properties]

# JDBC: Java Database Connector
spring.datasource.url=jdbc:h2:file:./data/${DATABASE_NAME}
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# Formats the API response
spring.jackson.serialization.indent-output=true

# JPA
spring.jpa.hibernate.ddl-auto=update

# DEVELOPMENT (only)
spring.jpa.show-sql=true

# H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Path Base (Endpoints)
server.port=${SERVER_PORT}
path.url=http://localhost:${server.port}

spring.security.user.name=${SPRING_USERNAME}
spring.security.user.password=${SPRING_PASSWORD}

# JWT
jwt.key=${JWT_KEY}
jwt.expiration-ms=${JWT_EXPIRATION_MS}
jwt.remember-expiration-ms=${JWT_REMEMBER_EXPIRATION_MS}
```

Observações:

- `spring.jpa.hibernate.ddl-auto=update` atualiza o schema automaticamente a cada execução.
- `spring.jpa.show-sql=true` deve ser usado **somente em desenvolvimento**.
- O console do H2 e o Swagger devem ser desabilitados ou protegidos em produção.

## Como executar

```bash
# Clonar o repositório
git clone https://github.com/oandredev/projeto-gerenciamento-de-saude.git
cd projeto-gerenciamento-de-saude/backend/gerenciamentodesaude

# Criar e preencher o arquivo .env (veja a seção Configuração)

# Executar a aplicação (ou direto pela IDE)
./mvnw spring-boot:run
```

No Windows, utilize `mvnw.cmd spring-boot:run`.

Após a inicialização (porta padrão `8080`):

| Recurso        | URL                                           |
| -------------- | --------------------------------------------- |
| API            | `http://localhost:8080`                       |
| Swagger UI     | `http://localhost:8080/swagger-ui/index.html` |
| OpenAPI (JSON) | `http://localhost:8080/v3/api-docs`           |
| Console H2     | `http://localhost:8080/h2-console`            |

Para acessar o console do H2, use a JDBC URL `jdbc:h2:file:./data/<DATABASE_NAME>` com as credenciais definidas em `DB_USERNAME` e `DB_PASSWORD`.

## Estrutura de pastas

```
health_management
    ├───advice
    ├───annotation
    ├───config
    ├───controller
    ├───dto
    │   ├───request
    │   │   ├───confirmation
    │   │   ├───profile
    │   │   ├───restriction
    │   │   ├───schedule
    │   │   └───user
    │   └───response
    │       ├───confirmation
    │       ├───general
    │       ├───occurrence
    │       ├───profile
    │       ├───restriction
    │       ├───schedule
    │       └───user
    ├───entity
    │   └───enums
    ├───repository
    ├───security
    ├───service
    └───util
```

| Pasta        | Responsabilidade                                                             |
| ------------ | ---------------------------------------------------------------------------- |
| `advice`     | Handler global de exceções (`@RestControllerAdvice`)                         |
| `annotation` | Anotações customizadas                                                       |
| `config`     | Configurações da aplicação (ex.: Spring Security)                            |
| `controller` | Endpoints REST                                                               |
| `dto`        | Objetos de entrada (`request`) e saída (`response`), organizados por domínio |
| `entity`     | Entidades JPA; `enums` guarda os tipos enumerados                            |
| `repository` | Interfaces de acesso a dados (Spring Data JPA)                               |
| `security`   | Filtro e componentes de autenticação JWT                                     |
| `service`    | Regras de negócio                                                            |
| `util`       | Classes utilitárias (ex.: cálculo de ocorrências)                            |

## API

A API segue o padrão REST, trafega JSON e é documentada com OpenAPI 3.1 (Springdoc).

### Documentação interativa (Swagger)

> **Atenção:** o link abaixo só funciona com a aplicação **em execução** localmente (veja a seção [Como executar](#como-executar)). Se a página não abrir, confirme se o backend está rodando e se a porta definida em `SERVER_PORT` é `8080`.

**[Abrir o Swagger UI](http://localhost:8080/swagger-ui/index.html#)**

Para testar os endpoints protegidos no Swagger, faça login em `POST /user/login`, copie o token retornado, clique em **Authorize** e informe o token (o prefixo `Bearer` é adicionado automaticamente pelo esquema `bearerAuth`).

### Convenções gerais

- **Base URL:** `http://localhost:8080`
- **Formato:** `application/json`
- **Datas e horários:** `date-time` em ISO 8601 e UTC (ex.: `2026-10-24T08:00:00Z`)
- **Autenticação:** header `Authorization: Bearer <token>` em todos os endpoints, exceto os públicos
- **Usuário autenticado:** o usuário sempre é obtido a partir do token JWT; nenhum endpoint recebe o `userId` na requisição
- **Atualizações:** não há `PATCH`. Os endpoints `PUT` recebem o objeto completo e sobrescrevem os dados
- **Exclusão lógica:** registros com `active = false` são considerados excluídos; o histórico é mantido, mas não aceita mais interação
- **Envelope de resposta:** veja [Padrão de respostas e tratamento de erros](#padrão-de-respostas-e-tratamento-de-erros). Os exemplos abaixo mostram apenas o conteúdo do campo `data`

### Resumo dos endpoints

| Recurso     | Método   | Rota                      | Descrição                                              | Público |
| ----------- | -------- | ------------------------- | ------------------------------------------------------ | ------- |
| Usuário     | `POST`   | `/user/signup`            | Cadastra um novo usuário                               | Sim     |
| Usuário     | `POST`   | `/user/login`             | Autentica e gera o token JWT                           | Sim     |
| Usuário     | `PUT`    | `/user`                   | Atualiza os dados do usuário autenticado               | Não     |
| Perfil      | `GET`    | `/profile`                | Lista os perfis do usuário autenticado                 | Não     |
| Perfil      | `POST`   | `/profile`                | Cria um perfil                                         | Não     |
| Perfil      | `PUT`    | `/profile/{id}`           | Atualiza um perfil                                     | Não     |
| Restrição   | `GET`    | `/restriction?profileId=` | Lista as restrições de um perfil                       | Não     |
| Restrição   | `POST`   | `/restriction`            | Cria uma restrição                                     | Não     |
| Restrição   | `PUT`    | `/restriction/{id}`       | Atualiza uma restrição                                 | Não     |
| Agendamento | `GET`    | `/schedule`               | Lista as regras de agendamento de um perfil            | Não     |
| Agendamento | `POST`   | `/schedule`               | Cria um agendamento                                    | Não     |
| Agendamento | `PUT`    | `/schedule`               | Atualiza um agendamento                                | Não     |
| Agendamento | `DELETE` | `/schedule/{id}`          | Desativa (exclusão lógica - Soft Delete) um agendamento              | Não     |
| Ocorrência  | `GET`    | `/occurrence`             | Lista as ocorrências calculadas em uma janela de tempo | Não     |
| Confirmação | `GET`    | `/confirmation`           | Lista as confirmações em um intervalo                  | Não     |
| Confirmação | `POST`   | `/confirmation`           | Confirma uma ocorrência                                | Não     |
| Confirmação | `DELETE` | `/confirmation/{id}`      | Desfaz  uma confirmação apagando ela                      | Não     |

---

### Usuário

#### `POST /user/signup`

Cadastra um novo usuário.

```json
{
  "name": "Maria Silva",
  "email": "maria@email.com",
  "password": "Teste123Aa."
}
```

Regras de validação (cadastro e atualização):

| Campo      | Regra                                                                                                                                                                  |
| ---------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `name`     | Somente letras, mínimo de 3 e máximo de 255 caracteres                                                                                                                 |
| `email`    | Formato `texto@texto.texto`, máximo de 255 caracteres; deve ser único                                                                                                  |
| `password` | Mínimo de 8 caracteres, com letra minúscula, maiúscula, número e caractere especial (pontuação ASCII); sem espaços e sem emojis; máximo de 72 bytes (limite do BCrypt) |

**Resposta:** `UserResponse`

```json
{
  "id": 1,
  "name": "Maria Silva",
  "email": "maria@email.com",
  "profiles": [],
  "active": true,
  "modifiedAt": "2026-10-24T12:00:00Z",
  "createdAt": "2026-10-24T12:00:00Z"
}
```

#### `POST /user/login`

Autentica o usuário e retorna o token JWT.

```json
{
  "email": "maria@email.com",
  "password": "Senha@123",
  "remember": false
}
```

| Campo      | Descrição                                                                                                                                                 |
| ---------- | --------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `remember` | Quando `true`, o token tem a duração definida em `JWT_REMEMBER_EXPIRATION_MS` (padrão: 30 dias); caso contrário, usa `JWT_EXPIRATION_MS` (padrão: 1 hora) |

**Resposta:** `UserResponse` (mesmo formato do cadastro) e o token JWT para uso no header `Authorization`.

#### `PUT /user`

Atualiza o usuário autenticado. Como não há `PATCH`, todos os campos são enviados, inclusive a senha.

```json
{
  "name": "Maria Souza",
  "email": "maria@email.com",
  "password": "Senha@123",
  "active": true
}
```

**Resposta:** `UserResponse`

---

### Perfil

Cada usuário pode ter vários perfis (modelo semelhante ao de plataformas de streaming). Não é permitido que um mesmo usuário tenha dois perfis com o mesmo nome.

#### `GET /profile`

Lista os perfis do usuário autenticado. Não possui parâmetros.

**Resposta:** lista de `ProfileResponse`

```json
[
  {
    "id": 1,
    "name": "Maria",
    "avatarId": 3,
    "active": true,
    "modifiedAt": "2026-10-24T12:00:00Z",
    "createdAt": "2026-10-24T12:00:00Z"
  }
]
```

#### `POST /profile`

```json
{
  "name": "Maria",
  "avatarId": 3
}
```

**Resposta:** `ProfileResponse`

#### `PUT /profile/{id}`

| Parâmetro | Local | Tipo  | Obrigatório | Descrição               |
| --------- | ----- | ----- | ----------- | ----------------------- |
| `id`      | path  | int64 | Sim         | Identificador do perfil |

```json
{
  "name": "Maria",
  "avatarId": 4,
  "active": true
}
```

**Resposta:** `ProfileResponse`

---

### Restrição

Registra alergias, remédios ou outras substâncias que o perfil não pode consumir.

**Enums**

| Campo      | Valores                                                                |
| ---------- | ---------------------------------------------------------------------- |
| `type`     | `MEDICATION`, `FOOD`, `SUBSTANCE`, `OTHER`                             |
| `severity` | `MILD`, `MODERATE`, `SEVERY` (grafia conforme o contrato atual da API) |

#### `GET /restriction`

| Parâmetro   | Local | Tipo  | Obrigatório | Descrição             |
| ----------- | ----- | ----- | ----------- | --------------------- |
| `profileId` | query | int64 | Sim         | Perfil das restrições |

**Resposta:** lista de `RestrictionResponse`

#### `POST /restriction`

```json
{
  "profileId": 1,
  "type": "MEDICATION",
  "severity": "MODERATE",
  "title": "Dipirona",
  "note": "Causa reação alérgica"
}
```

**Resposta:** `RestrictionResponse`

```json
{
  "id": 10,
  "profileId": 1,
  "type": "MEDICATION",
  "severity": "MODERATE",
  "title": "Dipirona",
  "note": "Causa reação alérgica",
  "active": true,
  "modifiedAt": "2026-10-24T12:00:00Z",
  "createdAt": "2026-10-24T12:00:00Z"
}
```

#### `PUT /restriction/{id}`

Mesmo corpo do `POST`, acrescido do campo `active`. O `id` é informado na rota.

---

### Agendamento

O agendamento define a **regra** de repetição (início, intervalo e fim). Cada agendamento possui dados específicos conforme o tipo, enviados no campo `details`.

**Enums**

| Campo          | Valores                                                        |
| -------------- | -------------------------------------------------------------- |
| `type`         | `CONSULTATION`, `EXAM`, `MEDICATION`                           |
| `intervalUnit` | `UNIQUE`, `MINUTE`, `HOUR`, `DAY`, `WEEK`, `MONTH`, `YEAR`     |
| `reminderUnit` | `MINUTE`, `HOUR`, `DAY`, `WEEK`                                |
| `dosageUnit`   | `AMPOULE`, `CAPSULE`, `DROPS`, `G`, `MCG`, `MG`, `ML`, `OTHER` |
| `stripe`       | `NONE`, `YELLOW`, `RED`, `BLACK`, `OTHER`                      |

**Campos principais**

| Campo                            | Tipo         | Descrição                                                     |
| -------------------------------- | ------------ | ------------------------------------------------------------- |
| `profileId`                      | int64        | Perfil dono do agendamento (somente no cadastro)              |
| `type`                           | enum         | Tipo do agendamento                                           |
| `title`                          | string       | Título                                                        |
| `startAt`                        | date-time    | Início (UTC)                                                  |
| `intervalValue` / `intervalUnit` | int32 / enum | Intervalo entre ocorrências; `UNIQUE` indica ocorrência única |
| `endAt`                          | date-time    | Fim da regra (exclusivo)                                      |
| `notes`                          | string       | Observações                                                   |
| `reminderValue` / `reminderUnit` | int32 / enum | Antecedência do lembrete                                      |
| `notifyEnabled`                  | boolean      | Ativa notificação                                             |
| `alarmEnabled`                   | boolean      | Ativa alarme                                                  |
| `details`                        | objeto       | Dados específicos do tipo (veja abaixo)                       |

**`details` para `CONSULTATION` e `EXAM`**

| Campo        | Tipo   |
| ------------ | ------ |
| `doctorName` | string |
| `location`   | string |

**`details` para `MEDICATION`**

| Campo              | Tipo            |
| ------------------ | --------------- |
| `medicineName`     | string          |
| `activeIngredient` | string          |
| `brand`            | string          |
| `dosageAmount`     | number (double) |
| `dosageUnit`       | enum            |
| `stripe`           | enum            |
| `photoUrl`         | string          |

#### `POST /schedule`

```json
{
  "profileId": 1,
  "type": "MEDICATION",
  "title": "Amoxicilina",
  "startAt": "2026-10-24T08:00:00Z",
  "intervalValue": 8,
  "intervalUnit": "HOUR",
  "endAt": "2026-10-31T08:00:00Z",
  "notes": "Tomar após as refeições",
  "reminderValue": 10,
  "reminderUnit": "MINUTE",
  "notifyEnabled": true,
  "alarmEnabled": true,
  "details": {
    "medicineName": "Amoxicilina",
    "activeIngredient": "Amoxicilina tri-hidratada",
    "brand": "Genérico",
    "dosageAmount": 500,
    "dosageUnit": "MG",
    "stripe": "RED",
    "photoUrl": "https://exemplo.com/caixa.jpg"
  }
}
```

**Resposta:** `ScheduleResponse` (mesmos campos acima, mais `id`, `active`, `modifiedAt` e `createdAt`).

#### `GET /schedule`

Lista as **regras** de agendamento de um perfil (não calcula ocorrências; para isso, use `/occurrence`).

| Parâmetro   | Local | Tipo           | Obrigatório | Descrição                  |
| ----------- | ----- | -------------- | ----------- | -------------------------- |
| `profileId` | query | int64          | Sim         | Perfil dos agendamentos    |
| `type`      | query | enum           | Não         | Filtra por tipo            |
| `order`     | query | `ASC` / `DESC` | Não         | Ordenação (padrão: `DESC`) |

**Resposta:** lista de `ScheduleResponse`

#### `PUT /schedule`

Atualiza um agendamento. O `id` é enviado no corpo, junto com os demais campos do `POST` (exceto `profileId`).

Quando o agendamento já possui confirmações, ele não é alterado: o registro atual é desativado (`active = false`) e um novo é criado com os dados atualizados, preservando o histórico. Sem confirmações, o registro é apenas atualizado.

**Resposta:** `ScheduleResponse`

#### `DELETE /schedule/{id}`

Desativa o agendamento (exclusão lógica). O histórico de confirmações é mantido.

| Parâmetro | Local | Tipo  | Obrigatório |
| --------- | ----- | ----- | ----------- |
| `id`      | path  | int64 | Sim         |

---

### Ocorrência

Uma ocorrência é cada execução prevista de um agendamento (ex.: cada dose de um remédio a cada 8 horas). As ocorrências **são calculadas** a partir da regra do agendamento, em UTC, e cruzadas com as confirmações existentes. Não são gravadas no banco.

#### `GET /occurrence`

| Parâmetro   | Local | Tipo           | Obrigatório | Descrição                          |
| ----------- | ----- | -------------- | ----------- | ---------------------------------- |
| `profileId` | query | int64          | Sim         | Perfil das ocorrências             |
| `reference` | query | date-time      | Sim         | Instante atual do aparelho, em UTC |
| `type`      | query | enum           | Não         | Filtra por tipo                    |
| `order`     | query | `ASC` / `DESC` | Não         | Ordenação (padrão: `DESC`)         |

Regras:

- A janela retornada considera **23 horas antes e 23 horas depois** do horário atual do servidor.
- O `reference` é comparado ao horário do servidor; se a diferença for maior que **30 minutos**, a requisição é recusada com erro `400` (evita o envio de horário local como se fosse UTC, logo o front end deve fazer a conversão do horario local e mandar o instant UTC).

**Status da ocorrência**

| Status      | Significado                            |
| ----------- | -------------------------------------- |
| `PENDING`   | Horário já passou e não foi confirmada |
| `CONFIRMED` | Ocorrência confirmada                  |
| `SCHEDULE`  | Ocorrência futura, ainda por vir       |

**Resposta:** lista de `OccurrenceResponse`

```json
[
  {
    "confirmationId": null,
    "scheduleId": 5,
    "title": "Amoxicilina",
    "type": "MEDICATION",
    "scheduledAt": "2026-10-24T08:00:00Z",
    "status": "PENDING",
    "confirmedAt": null
  }
]
```

---

### Confirmação

A confirmação registra que uma ocorrência foi cumprida. Se a linha existe, a ocorrência foi confirmada. O histórico é imutável fora da janela de edição.

#### `POST /confirmation`

```json
{
  "scheduleId": 5,
  "scheduleAt": "2026-10-24T08:00:00Z"
}
```

Validações:

- `scheduleAt` deve corresponder a uma ocorrência válida do agendamento (`scheduleId`) ativo e pertencente ao usuário autenticado.
- A confirmação só é aceita dentro da janela de **24 horas antes até 24 horas depois** do horário agendado.

**Resposta:** `ConfirmationResponse`

```json
{
  "id": 20,
  "scheduleId": 5,
  "scheduleAt": "2026-10-24T08:00:00Z",
  "confirmedAt": "2026-10-24T08:05:00Z"
}
```

#### `GET /confirmation`

Consulta o histórico de confirmações em um intervalo.

| Parâmetro   | Local | Tipo           | Obrigatório | Descrição                  |
| ----------- | ----- | -------------- | ----------- | -------------------------- |
| `profileId` | query | int64          | Sim         | Perfil das confirmações    |
| `start`     | query | date-time      | Sim         | Início do intervalo        |
| `end`       | query | date-time      | Sim         | Fim do intervalo           |
| `order`     | query | `ASC` / `DESC` | Não         | Ordenação (padrão: `DESC`) |

**Resposta:** lista de `ConfirmationResponse`

#### `DELETE /confirmation/{id}`

Desfaz a confirmação, dentro da mesma janela de 24 horas.

| Parâmetro | Local | Tipo  | Obrigatório |
| --------- | ----- | ----- | ----------- |
| `id`      | path  | int64 | Sim         |

---

### Códigos de resposta

| Código | Quando ocorre                                                               |
| ------ | --------------------------------------------------------------------------- |
| `200`  | Operação realizada com sucesso                                              |
| `400`  | Dados inválidos, regra de negócio violada ou `reference` fora da tolerância |
| `401`  | Token ausente, inválido ou expirado                                         |
| `404`  | Recurso não encontrado                                                      |

Para os esquemas completos de cada objeto, consulte o [Swagger UI](http://localhost:8080/swagger-ui/index.html#) com a aplicação em execução.

## Autenticação e segurança

- **Autenticação:** JWT (stateless)
- **Criptografia de senha:** BCrypt
- **Expiração do token:** definida em `JWT_EXPIRATION_MS` (e `JWT_REMEMBER_EXPIRATION_MS` quando o login usa "lembrar de mim")

### Fluxo

1. O cliente envia as credenciais para `POST /user/login`.
2. A API valida e devolve um token JWT.
3. O cliente envia o token nas próximas requisições:

```
Authorization: Bearer <token>
```

### Endpoints públicos

Todos os endpoints exigem autenticação, exceto:

- `/user/login`
- `/user/signup`
- `/h2-console/**`
- `/swagger-ui.html`
- `/swagger-ui/**`
- `/v3/api-docs/**`

## Padrão de respostas e tratamento de erros

Todas as respostas seguem um formato padrão (encapsulamento), e um handler global (`@RestControllerAdvice`) intercepta as exceções, garantindo que stack traces e dados internos **não vazem** para o cliente.

### Sucesso

```json
{
  "message": "Operação realizada com sucesso",
  "data": {}
}
```

### Erro

```json
{
  "message": "Recurso não encontrado"
}
```