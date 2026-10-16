# 💸 Gastos API

![CI](https://github.com/AaronSalvador/gastos-api/actions/workflows/ci.yml/badge.svg)

API REST para registrar y analizar gastos personales. Cada usuario tiene su cuenta y solo puede ver y modificar sus propios gastos.

## Características

- Registro e inicio de sesión con **JWT** de 1 hora (contraseñas cifradas con BCrypt)
- Sin secretos en el código: claves y contraseñas por variables de entorno
- **CRUD** completo de gastos, con paginación y filtro por categoría
- **Resumen** del total gastado y total por categoría
- **Validaciones** de datos con mensajes claros por campo
- **Manejo de errores** centralizado (400, 401, 404, 409, 500) con respuestas JSON consistentes
- Aislamiento por usuario: un gasto de otra persona responde 404
- **Asistente de IA** (opcional): endpoint `POST /api/assistant/chat` que responde con un resumen de tus gastos como contexto
- Documentación interactiva con **Swagger UI** (se activa con `SWAGGER_ENABLED=true`)
- Pruebas unitarias con **JUnit 5 y Mockito**
- **Docker Compose** para levantar API y base de datos con un solo comando
- Integración continua con **GitHub Actions**

## Tecnologías

Java 17 · Spring Boot 3 · Spring Security · Spring Data JPA · PostgreSQL · JWT (jjwt) · Swagger / OpenAPI · Docker · Maven

## Asistente de IA (opcional)

El endpoint `POST /api/assistant/chat` (requiere token) envía la pregunta del usuario y un **resumen de sus gastos** a la API de Anthropic y devuelve la respuesta.

- Se activa definiendo la variable de entorno **`ANTHROPIC_API_KEY`** (en IntelliJ: *Run → Edit Configurations → Environment variables*, o en `application-local.yml` bajo `app.assistant.api-key`).
- El modelo se cambia con `ASSISTANT_MODEL` (por defecto `claude-haiku-5-5`).
- Si no hay clave, la API responde **503** y el frontend usa su "modo básico" (respuestas locales sin IA), así que la app funciona igual.
- Límite de **15 preguntas por minuto** por usuario. La clave nunca se envía al navegador ni se registra en los logs.

## Cómo ejecutarlo

La API **no trae contraseñas ni claves escritas en el código**: las lee de variables de entorno.

### Con Docker

```bash
git clone https://github.com/AaronSalvador/gastos-api.git
cd gastos-api
cp .env.example .env      # luego completa DB_PASSWORD y JWT_SECRET
docker compose up --build
```

La API queda en `http://localhost:8080` y Swagger en **http://localhost:8080/swagger-ui.html**.

### Sin Docker (IntelliJ)

Necesitas Java 17 o superior, Maven y PostgreSQL con una base llamada `gastos`.

1. Crea la base: `CREATE DATABASE gastos;`
2. Copia `src/main/resources/application-local.yml.example` como `application-local.yml` y completa tu contraseña y una clave JWT (este archivo está en `.gitignore`).
3. Ejecuta con el perfil `local`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

En IntelliJ: *Edit Configurations → Active profiles = `local`*.

### Variables de entorno

| Variable | Obligatoria | Descripción |
|----------|-------------|-------------|
| `DB_PASSWORD` | Sí | Contraseña de PostgreSQL |
| `JWT_SECRET` | Sí | Clave del JWT en Base64 (mínimo 32 bytes) |
| `DB_URL` | No | Por defecto `jdbc:postgresql://localhost:5432/gastos` |
| `DB_USER` | No | Por defecto `postgres` |
| `JWT_EXPIRATION_MS` | No | Duración del token (por defecto 1 hora) |
| `CORS_ORIGINS` | No | Orígenes del frontend permitidos |
| `SWAGGER_ENABLED` | No | `true` para activar Swagger (apagado por defecto) |

## Endpoints

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| POST | `/api/auth/register` | Crear cuenta | No |
| POST | `/api/auth/login` | Iniciar sesión | No |
| POST | `/api/expenses` | Registrar un gasto | Sí |
| GET | `/api/expenses?category=COMIDA&page=0&size=10` | Listar mis gastos | Sí |
| GET | `/api/expenses/{id}` | Ver un gasto | Sí |
| PUT | `/api/expenses/{id}` | Actualizar un gasto | Sí |
| DELETE | `/api/expenses/{id}` | Eliminar un gasto | Sí |
| GET | `/api/expenses/summary` | Total y total por categoría | Sí |

Categorías: `COMIDA`, `TRANSPORTE`, `EDUCACION`, `ENTRETENIMIENTO`, `SALUD`, `OTROS`.

## Ejemplo de uso

```bash
# 1. Crear cuenta (devuelve el token)
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Aarón Laura","email":"aaron@correo.com","password":"clave12345"}'

# 2. Registrar un gasto (usa el token recibido)
curl -X POST http://localhost:8080/api/expenses \
  -H "Authorization: Bearer TU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"description":"Almuerzo","amount":15.50,"category":"COMIDA","date":"2025-01-15"}'

# 3. Ver el resumen
curl http://localhost:8080/api/expenses/summary -H "Authorization: Bearer TU_TOKEN"
```

En Swagger UI pulsa **Authorize**, pega el token y prueba todo desde el navegador.

## Pruebas

```bash
mvn test
```

## Estructura

```
src/main/java/com/aaronsalvador/gastos
├── config/        Seguridad y Swagger
├── controller/    Endpoints REST
├── dto/           Objetos de entrada y salida
├── exception/     Errores y manejador global
├── model/         Entidades JPA
├── repository/    Acceso a datos
├── security/      JWT y filtro de autenticación
└── service/       Lógica de negocio
```

## Posibles mejoras

- Migraciones de base de datos con Flyway
- Pruebas de integración con Testcontainers
- Refresh tokens y roles
- Presupuestos mensuales por categoría
- Frontend en Angular que consuma esta API

## Autor

**Aarón Salvador Laura García** · [GitHub](https://github.com/AaronSalvador) · [Portafolio](https://aaronsalvador.github.io)
