# Msvc Users

Microservicio de usuarios desarrollado con Spring Boot y Postgres.

## Tabla de Contenidos
1. [Descripción](#descripción)
2. [Características](#características)
3. [Tecnologías y Herramientas](#tecnologías-y-herramientas)
4. [Requisitos Previos](#requisitos-previos)
5. [Configuración del Entorno](#configuración-del-entorno)
7. [Ejecución del Proyecto](#ejecución-del-proyecto)
    - [Iniciar con Docker Compose](#iniciar-con-docker-compose)
    - [Iniciar localmente (sin Docker)](#iniciar-localmente-sin-docker)
8. [Uso y Endpoints Disponibles](#uso-y-endpoints-disponibles)
9. [Pruebas](#pruebas)
10. [Contribuir](#contribuir)

---

## Descripción

Este proyecto provee un microservicio de usuarios que expone endpoints REST para la administración de datos de usuarios (CRUD). Se conecta a una base de datos PostgreSQL y está preparado para ser desplegado en contenedores Docker.

---

## Características

- **Spring Boot (versión 3.4.3)**
- **Persistencia con PostgreSQL**
- **Endpoints REST** para crear, leer, actualizar y eliminar usuarios
- **Swagger / OpenAPI** para documentación de la API
- Integración y despliegue mediante **Docker** y **Docker Compose**
- Variables de entorno para facilitar la configuración

---

## Tecnologías y Herramientas

- [Java 17](https://adoptium.net/)
- [Spring Boot 3.4.3](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/)
- [PostgreSQL 15](https://www.postgresql.org/)
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Swagger / OpenAPI](https://swagger.io/)

---

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalado localmente:

1. **Java 17**
2. **Maven** (opcional, si deseas compilar/ejecutar la app localmente sin Docker)
3. **Docker** (versión 20 o superior recomendada)
4. **Docker Compose**

---

## Configuración del Entorno

El proyecto utiliza variables de entorno que se pueden establecer de dos maneras principales:

1. **Archivo `.env`** (método recomendado):  
   Contiene las variables de entorno necesarias (puertos, credenciales de DB, etc.).
   ```bash
   # Configuración de la aplicación
   SPRING_APPLICATION_NAME=
   SERVER_PORT=
   API_PREFIX=

   # Configuración de la base de datos
   DB_NAME=
   DB_USERNAME=
   DB_PASSWORD=
   ```

2. **Directamente en `docker-compose.yml`**:
    - Se pueden declarar en la sección `environment` si no deseas usar un archivo `.env`.

> **Nota:** Si vas a ejecutar la aplicación fuera de Docker, también necesitarás configurar el archivo `application.properties` o un `.env` que Spring cargue automáticamente.

---



- **Dockerfile**: Define la imagen de Docker para la aplicación Spring Boot.
- **docker-compose.yml**: Configura los servicios (app y postgres) y sus relaciones.
- **.env**: Variables de entorno que se usarán en `docker-compose.yml`.
- **pom.xml**: Archivo de Maven con las dependencias y configuración del proyecto.
- **src/main/java**: Código fuente de la aplicación (controladores, servicios, modelos, etc.).

---

## Ejecución del Proyecto

### Iniciar con Docker Compose

1. **Clona este repositorio** o descarga el código en tu máquina local.
2. **Copia o ajusta** las variables de entorno en el archivo `.env` (si es necesario).
3. En la raíz del proyecto, ejecuta:
   ```bash
   docker-compose up --build
   ```
   Este comando:
    - Construirá la imagen de la aplicación (usando el Dockerfile).
    - Levantará el contenedor de la aplicación y el contenedor de PostgreSQL.
4. Una vez finalizado el proceso, deberías ver logs de ambos contenedores.
5. Accede a la aplicación en tu navegador o herramienta de prueba (Postman, cURL) con la ruta:
   ```
   http://localhost:9090/api   # Según tu SERVER_PORT y API_PREFIX
   ```

### Iniciar localmente (sin Docker)

Si prefieres o necesitas levantar la aplicación en tu máquina local (y no en contenedores):

1. **Asegúrate de tener una instancia de PostgreSQL** corriendo y crea la base de datos `prueba_tecnica` (o la que hayas configurado en el `.env`).
2. **Configura las credenciales de tu base de datos** en el `application.properties`, `.env` u otro archivo de configuración.
3. Ejecuta:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
4. La aplicación se iniciará por defecto en el puerto `9090` (configurable), en la ruta:
   ```
   http://localhost:9090/api
   ```

---

## Uso y Endpoints Disponibles

Una vez que el microservicio está corriendo, puedes acceder a la documentación de la API (Swagger) en:

```
http://localhost:9090/swagger-ui.html
```
o
```
http://localhost:9090/swagger-ui/index.html
```
(Según la configuración de la versión de Springdoc).

Los endpoints principales incluyen (ejemplos):
1. **GET** `/api/users`: Listar todos los usuarios
2. **GET** `/api/users/{id}`: Buscar un usuario por su ID
3. **POST** `/api/users`: Crear un nuevo usuario
4. **PUT** `/api/users/{id}`: Actualizar un usuario existente
5. **DELETE** `/api/users/{id}`: Eliminar un usuario

---

## Pruebas

Para ejecutar las pruebas unitarias y de integración definidas en el proyecto:

```bash
mvn test
```

---

