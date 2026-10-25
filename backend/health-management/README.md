<p align="right"><a href="README.pt.md">Português</a> | <b>English</b></p>

# Health Management - Backend

REST API for the academic project **Health Management**, responsible for managing users, profiles, restrictions, schedules (medications, appointments and exams), occurrences and confirmations.

## Maintainer

- **André Rodrigues** - [github.com/oandredev](https://github.com/oandredev)

## Technologies and dependencies

| Technology           | Version / Artifact                   | Purpose                          |
| -------------------- | ------------------------------------ | -------------------------------- |
| Java                 | 17                                   | Language                         |
| Spring Boot          | 4.1.1 (`spring-boot-starter-parent`) | Base framework                   |
| Spring Web MVC       | `spring-boot-starter-webmvc`         | REST layer                       |
| Spring Data JPA      | `spring-boot-starter-data-jpa`       | Persistence (Hibernate)          |
| H2 Database          | `com.h2database:h2`                  | File-based database              |
| H2 Console           | `spring-boot-h2console`              | Database web interface           |
| Spring Security      | `spring-boot-starter-security`       | Authentication and authorization |
| JWT (jjwt)           | 0.12.6 (`api`, `impl`, `jackson`)    | Token generation and validation  |
| Springdoc OpenAPI    | 3.2.0                                | Documentation (Swagger UI)       |
| Spring REST Client   | `spring-boot-starter-restclient`     | HTTP client                      |
| Spring Boot DevTools | runtime / optional                   | Hot reload for development       |

Test dependencies: `spring-boot-starter-data-jpa-test`, `spring-boot-starter-restclient-test` and `spring-boot-starter-webmvc-test`.

## Prerequisites

- JDK 17 or higher
- Maven 3.9+ (or the project's `mvnw` wrapper)

## Configuration

### 1. `.env` file

Create a `.env` file at the root of the backend module (same level as `pom.xml`):

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

| Variable                              | Description                                         |
| ------------------------------------- | --------------------------------------------------- |
| `DATABASE_NAME`                       | Name of the H2 database file (created in `./data/`) |
| `DB_USERNAME` / `DB_PASSWORD`         | Database credentials                                |
| `SPRING_USERNAME` / `SPRING_PASSWORD` | Username and password for Spring Security           |
| `JWT_KEY`                             | Secret key used to sign JWT tokens (32 bytes)       |
| `JWT_EXPIRATION_MS`                   | Default token lifetime (in milliseconds)            |
| `JWT_REMEMBER_EXPIRATION_MS`          | Token lifetime when the login uses "remember me"    |
| `SERVER_PORT`                         | Application port. Default: 8080                     |

> The `.env` file contains sensitive data and **must not be versioned**. Add it to `.gitignore`.

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

Notes:

- `spring.jpa.hibernate.ddl-auto=update` updates the schema automatically on every run.
- `spring.jpa.show-sql=true` should be used **in development only**.
- The H2 console and Swagger should be disabled or protected in production.

## How to run

```bash
# Clone the repository
git clone https://github.com/oandredev/projeto-gerenciamento-de-saude.git
cd projeto-gerenciamento-de-saude/backend/gerenciamentodesaude

# Create and fill in the .env file (see the Configuration section)

# Run the application
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run`.

After startup (default port `8080`):

| Resource       | URL                                           |
| -------------- | --------------------------------------------- |
| API            | `http://localhost:8080`                       |
| Swagger UI     | `http://localhost:8080/swagger-ui/index.html` |
| OpenAPI (JSON) | `http://localhost:8080/v3/api-docs`           |
| H2 Console     | `http://localhost:8080/h2-console`            |

To access the H2 console, use the JDBC URL `jdbc:h2:file:./data/<DATABASE_NAME>` with the credentials defined in `DB_USERNAME` and `DB_PASSWORD`.

## Folder structure

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

| Folder       | Responsibility                                                         |
| ------------ | ---------------------------------------------------------------------- |
| `advice`     | Global exception handler (`@RestControllerAdvice`)                     |
| `annotation` | Custom annotations                                                     |
| `config`     | Application configuration (e.g. Spring Security)                       |
| `controller` | REST endpoints                                                         |
| `dto`        | Input (`request`) and output (`response`) objects, organized by domain |
| `entity`     | JPA entities; `enums` holds the enumerated types                       |
| `repository` | Data access interfaces (Spring Data JPA)                               |
| `security`   | JWT authentication filter and components                               |
| `service`    | Business rules                                                         |
| `util`       | Utility classes (e.g. occurrence calculation)                          |

## API

The API follows REST conventions, exchanges JSON and is documented with OpenAPI 3.1 (Springdoc).

### Interactive documentation (Swagger)

> **Note:** the link below only works while the application is **running** locally (see the [How to run](#how-to-run) section). If the page does not open, make sure the backend is running and that the port defined in `SERVER_PORT` is `8080`.

**[Open Swagger UI](http://localhost:8080/swagger-ui/index.html#)**

To test protected endpoints in Swagger, log in at `POST /user/login`, copy the returned token, click **Authorize** and paste the token (the `Bearer` prefix is added automatically by the `bearerAuth` scheme).

### General conventions

- **Base URL:** `http://localhost:8080`
- **Format:** `application/json`
- **Dates and times:** `date-time` in ISO 8601 and UTC (e.g. `2026-10-24T08:00:00Z`)
- **Authentication:** `Authorization: Bearer <token>` header on all endpoints except the public ones
- **Authenticated user:** the user is always taken from the JWT; no endpoint receives a `userId` in the request
- **Updates:** there is no `PATCH`. `PUT` endpoints receive the full object and overwrite the data
- **Soft delete:** records with `active = false` are considered deleted; history is kept but no longer accepts interaction
- **Response envelope:** see [Response pattern and error handling](#response-pattern-and-error-handling). The examples below show only the content of the `data` field

### Endpoint summary

| Resource     | Method   | Route                     | Description                                    | Public |
| ------------ | -------- | ------------------------- | ---------------------------------------------- | ------ |
| User         | `POST`   | `/user/signup`            | Registers a new user                           | Yes    |
| User         | `POST`   | `/user/login`             | Authenticates and issues the JWT               | Yes    |
| User         | `PUT`    | `/user`                   | Updates the authenticated user                 | No     |
| Profile      | `GET`    | `/profile`                | Lists the authenticated user's profiles        | No     |
| Profile      | `POST`   | `/profile`                | Creates a profile                              | No     |
| Profile      | `PUT`    | `/profile/{id}`           | Updates a profile                              | No     |
| Restriction  | `GET`    | `/restriction?profileId=` | Lists a profile's restrictions                 | No     |
| Restriction  | `POST`   | `/restriction`            | Creates a restriction                          | No     |
| Restriction  | `PUT`    | `/restriction/{id}`       | Updates a restriction                          | No     |
| Schedule     | `GET`    | `/schedule`               | Lists a profile's schedule rules               | No     |
| Schedule     | `POST`   | `/schedule`               | Creates a schedule                             | No     |
| Schedule     | `PUT`    | `/schedule`               | Updates a schedule                             | No     |
| Schedule     | `DELETE` | `/schedule/{id}`          | Deactivates (soft delete) a schedule           | No     |
| Occurrence   | `GET`    | `/occurrence`             | Lists occurrences calculated for a time window | No     |
| Confirmation | `GET`    | `/confirmation`           | Lists confirmations within an interval         | No     |
| Confirmation | `POST`   | `/confirmation`           | Confirms an occurrence                         | No     |
| Confirmation | `DELETE` | `/confirmation/{id}`      | Undoes (unchecks) a confirmation               | No     |

---

### User

#### `POST /user/signup`

Registers a new user.

```json
{
  "name": "Maria Silva",
  "email": "maria@email.com",
  "password": "Senha@123"
}
```

Validation rules (signup and update):

| Field      | Rule                                                                                                                                                            |
| ---------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `name`     | Letters only, minimum 3 and maximum 255 characters                                                                                                              |
| `email`    | Format `text@text.text`, maximum 255 characters; must be unique                                                                                                 |
| `password` | Minimum 8 characters, with lowercase, uppercase, a number and a special character (ASCII punctuation); no spaces and no emojis; maximum 72 bytes (BCrypt limit) |

**Response:** `UserResponse`

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

Authenticates the user and returns the JWT.

```json
{
  "email": "maria@email.com",
  "password": "Senha@123",
  "remember": false
}
```

| Field      | Description                                                                                                                                                 |
| ---------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `remember` | When `true`, the token lasts as long as defined in `JWT_REMEMBER_EXPIRATION_MS` (default: 30 days); otherwise it uses `JWT_EXPIRATION_MS` (default: 1 hour) |

**Response:** `UserResponse` (same format as signup) and the JWT to be used in the `Authorization` header.

#### `PUT /user`

Updates the authenticated user. Since there is no `PATCH`, all fields are sent, including the password.

```json
{
  "name": "Maria Souza",
  "email": "maria@email.com",
  "password": "Senha@123",
  "active": true
}
```

**Response:** `UserResponse`

---

### Profile

Each user can have multiple profiles (similar to streaming platforms). A user cannot have two profiles with the same name.

#### `GET /profile`

Lists the authenticated user's profiles. No parameters.

**Response:** list of `ProfileResponse`

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

**Response:** `ProfileResponse`

#### `PUT /profile/{id}`

| Parameter | Location | Type  | Required | Description        |
| --------- | -------- | ----- | -------- | ------------------ |
| `id`      | path     | int64 | Yes      | Profile identifier |

```json
{
  "name": "Maria",
  "avatarId": 4,
  "active": true
}
```

**Response:** `ProfileResponse`

---

### Restriction

Records allergies, medications or other substances the profile must not consume.

**Enums**

| Field      | Values                                                                 |
| ---------- | ---------------------------------------------------------------------- |
| `type`     | `MEDICATION`, `FOOD`, `SUBSTANCE`, `OTHER`                             |
| `severity` | `MILD`, `MODERATE`, `SEVERY` (spelling as in the current API contract) |

#### `GET /restriction`

| Parameter   | Location | Type  | Required | Description                        |
| ----------- | -------- | ----- | -------- | ---------------------------------- |
| `profileId` | query    | int64 | Yes      | Profile that owns the restrictions |

**Response:** list of `RestrictionResponse`

#### `POST /restriction`

```json
{
  "profileId": 1,
  "type": "MEDICATION",
  "severity": "MODERATE",
  "title": "Dipyrone",
  "note": "Causes an allergic reaction"
}
```

**Response:** `RestrictionResponse`

```json
{
  "id": 10,
  "profileId": 1,
  "type": "MEDICATION",
  "severity": "MODERATE",
  "title": "Dipyrone",
  "note": "Causes an allergic reaction",
  "active": true,
  "modifiedAt": "2026-10-24T12:00:00Z",
  "createdAt": "2026-10-24T12:00:00Z"
}
```

#### `PUT /restriction/{id}`

Same body as `POST`, plus the `active` field. The `id` is passed in the route.

---

### Schedule

A schedule defines the repetition **rule** (start, interval and end). Each schedule carries type-specific data, sent in the `details` field.

**Enums**

| Field          | Values                                                         |
| -------------- | -------------------------------------------------------------- |
| `type`         | `CONSULTATION`, `EXAM`, `MEDICATION`                           |
| `intervalUnit` | `UNIQUE`, `MINUTE`, `HOUR`, `DAY`, `WEEK`, `MONTH`, `YEAR`     |
| `reminderUnit` | `MINUTE`, `HOUR`, `DAY`, `WEEK`                                |
| `dosageUnit`   | `AMPOULE`, `CAPSULE`, `DROPS`, `G`, `MCG`, `MG`, `ML`, `OTHER` |
| `stripe`       | `NONE`, `YELLOW`, `RED`, `BLACK`, `OTHER`                      |

**Main fields**

| Field                            | Type         | Description                                                      |
| -------------------------------- | ------------ | ---------------------------------------------------------------- |
| `profileId`                      | int64        | Profile that owns the schedule (creation only)                   |
| `type`                           | enum         | Schedule type                                                    |
| `title`                          | string       | Title                                                            |
| `startAt`                        | date-time    | Start (UTC)                                                      |
| `intervalValue` / `intervalUnit` | int32 / enum | Interval between occurrences; `UNIQUE` means a single occurrence |
| `endAt`                          | date-time    | End of the rule (exclusive)                                      |
| `notes`                          | string       | Notes                                                            |
| `reminderValue` / `reminderUnit` | int32 / enum | Reminder lead time                                               |
| `notifyEnabled`                  | boolean      | Enables notification                                             |
| `alarmEnabled`                   | boolean      | Enables alarm                                                    |
| `details`                        | object       | Type-specific data (see below)                                   |

**`details` for `CONSULTATION` and `EXAM`**

| Field        | Type   |
| ------------ | ------ |
| `doctorName` | string |
| `location`   | string |

**`details` for `MEDICATION`**

| Field              | Type            |
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
  "title": "Amoxicillin",
  "startAt": "2026-10-24T08:00:00Z",
  "intervalValue": 8,
  "intervalUnit": "HOUR",
  "endAt": "2026-10-31T08:00:00Z",
  "notes": "Take after meals",
  "reminderValue": 10,
  "reminderUnit": "MINUTE",
  "notifyEnabled": true,
  "alarmEnabled": true,
  "details": {
    "medicineName": "Amoxicillin",
    "activeIngredient": "Amoxicillin trihydrate",
    "brand": "Generic",
    "dosageAmount": 500,
    "dosageUnit": "MG",
    "stripe": "RED",
    "photoUrl": "https://example.com/box.jpg"
  }
}
```

**Response:** `ScheduleResponse` (same fields as above, plus `id`, `active`, `modifiedAt` and `createdAt`).

#### `GET /schedule`

Lists a profile's schedule **rules** (it does not calculate occurrences; use `/occurrence` for that).

| Parameter   | Location | Type           | Required | Description                     |
| ----------- | -------- | -------------- | -------- | ------------------------------- |
| `profileId` | query    | int64          | Yes      | Profile that owns the schedules |
| `type`      | query    | enum           | No       | Filters by type                 |
| `order`     | query    | `ASC` / `DESC` | No       | Sort order (default: `DESC`)    |

**Response:** list of `ScheduleResponse`

#### `PUT /schedule`

Updates a schedule. The `id` is sent in the body along with the other `POST` fields (except `profileId`).

When the schedule already has confirmations, it is not modified: the current record is deactivated (`active = false`) and a new one is created with the updated data, preserving history. Without confirmations, the record is simply updated.

**Response:** `ScheduleResponse`

#### `DELETE /schedule/{id}`

Deactivates the schedule (soft delete). The confirmation history is kept.

| Parameter | Location | Type  | Required |
| --------- | -------- | ----- | -------- |
| `id`      | path     | int64 | Yes      |

---

### Occurrence

An occurrence is each expected execution of a schedule (e.g. each dose of a medication taken every 8 hours). Occurrences are **calculated** from the schedule rule, in UTC, and cross-checked with existing confirmations. They are not stored in the database.

#### `GET /occurrence`

| Parameter   | Location | Type           | Required | Description                           |
| ----------- | -------- | -------------- | -------- | ------------------------------------- |
| `profileId` | query    | int64          | Yes      | Profile that owns the occurrences     |
| `reference` | query    | date-time      | Yes      | Current instant of the device, in UTC |
| `type`      | query    | enum           | No       | Filters by type                       |
| `order`     | query    | `ASC` / `DESC` | No       | Sort order (default: `DESC`)          |

Rules:

- The returned window covers **23 hours before and 23 hours after** the server's current time.
- `reference` is compared with the server time; if the difference is greater than **30 minutes**, the request is rejected with a `400` error (this prevents local time from being sent as if it were UTC).

**Occurrence status**

| Status      | Meaning                                  |
| ----------- | ---------------------------------------- |
| `PENDING`   | Time has passed and it was not confirmed |
| `CONFIRMED` | Occurrence confirmed                     |
| `SCHEDULE`  | Future occurrence, still to come         |

**Response:** list of `OccurrenceResponse`

```json
[
  {
    "confirmationId": null,
    "scheduleId": 5,
    "title": "Amoxicillin",
    "type": "MEDICATION",
    "scheduledAt": "2026-10-24T08:00:00Z",
    "status": "PENDING",
    "confirmedAt": null
  }
]
```

---

### Confirmation

A confirmation records that an occurrence was fulfilled. If the row exists, the occurrence is confirmed. History is immutable outside the edit window.

#### `POST /confirmation`

```json
{
  "scheduleId": 5,
  "scheduleAt": "2026-10-24T08:00:00Z"
}
```

Validations:

- `scheduleAt` must match a valid occurrence of the active schedule (`scheduleId`) owned by the authenticated user.
- The confirmation is only accepted within a window of **24 hours before to 24 hours after** the scheduled time.

**Response:** `ConfirmationResponse`

```json
{
  "id": 20,
  "scheduleId": 5,
  "scheduleAt": "2026-10-24T08:00:00Z",
  "confirmedAt": "2026-10-24T08:05:00Z"
}
```

#### `GET /confirmation`

Queries the confirmation history within an interval.

| Parameter   | Location | Type           | Required | Description                         |
| ----------- | -------- | -------------- | -------- | ----------------------------------- |
| `profileId` | query    | int64          | Yes      | Profile that owns the confirmations |
| `start`     | query    | date-time      | Yes      | Start of the interval               |
| `end`       | query    | date-time      | Yes      | End of the interval                 |
| `order`     | query    | `ASC` / `DESC` | No       | Sort order (default: `DESC`)        |

**Response:** list of `ConfirmationResponse`

#### `DELETE /confirmation/{id}`

Undoes the confirmation, within the same 24-hour window.

| Parameter | Location | Type  | Required |
| --------- | -------- | ----- | -------- |
| `id`      | path     | int64 | Yes      |

---

### Response codes

| Code  | When it occurs                                                            |
| ----- | ------------------------------------------------------------------------- |
| `200` | Operation completed successfully                                          |
| `400` | Invalid data, business rule violated or `reference` outside the tolerance |
| `401` | Missing, invalid or expired token                                         |
| `404` | Resource not found                                                        |

For the full schema of each object, see the [Swagger UI](http://localhost:8080/swagger-ui/index.html#) with the application running.

## Authentication and security

- **Authentication:** JWT (stateless)
- **Password encryption:** BCrypt
- **Token expiration:** defined in `JWT_EXPIRATION_MS` (and `JWT_REMEMBER_EXPIRATION_MS` when the login uses "remember me")

### Flow

1. The client sends the credentials to `POST /user/login`.
2. The API validates them and returns a JWT.
3. The client sends the token on subsequent requests:

```
Authorization: Bearer <token>
```

### Public endpoints

All endpoints require authentication, except:

- `/user/login`
- `/user/signup`
- `/h2-console/**`
- `/swagger-ui.html`
- `/swagger-ui/**`
- `/v3/api-docs/**`

## Response pattern and error handling

All responses follow a standard format (envelope), and a global handler (`@RestControllerAdvice`) intercepts exceptions, ensuring that stack traces and internal data **do not leak** to the client.

> The `message` values are returned by the API in Portuguese.

### Success

```json
{
  "message": "Operação realizada com sucesso",
  "data": {}
}
```

### Error

```json
{
  "message": "Recurso não encontrado"
}
```
