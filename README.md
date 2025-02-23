# Microservicio de Usuarios

Microservicio para gestión de usuarios construido con Spring Boot y PostgreSQL.

## Descripción

API REST que proporciona endpoints para administrar usuarios (CRUD), usando Spring Boot 3.4.3 y PostgreSQL como base de datos.

## Tecnologías

- Java 17
- Spring Boot 3.4.3
- PostgreSQL 15
- Docker & Docker Compose
- Swagger/OpenAPI

## Comenzar

### Requisitos

- Java 17
- Docker y Docker Compose (para ejecución containerizada)
- Maven (opcional, para ejecución local)

### Variables de Entorno

Crea un archivo `.env` con:

```bash
# App
SPRING_APPLICATION_NAME=
SERVER_PORT=
API_PREFIX=

# Base de datos
DB_NAME=
DB_USERNAME=
DB_PASSWORD=
```

### Ejecutar

**Con Docker:**
```bash
docker-compose up --build
```

**Local:**
1. Asegúrate de tener PostgreSQL corriendo
2. Configura las credenciales en `application.properties`
3. Ejecuta:
```bash
mvn spring-boot:run
```

## API Endpoints

Documentación completa disponible en: `http://localhost:9090/swagger-ui.html`

Endpoints principales:
- `GET /api/users`: Listar usuarios
- `GET /api/users/{id}`: Obtener usuario
- `POST /api/users`: Crear usuario
- `PUT /api/users/{id}`: Actualizar usuario
- `DELETE /api/users/{id}`: Eliminar usuario

## Pruebas

```bash
mvn test
```