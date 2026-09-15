# FlowerServices API

API RESTful para la contratación y agendamiento de servicios técnicos a domicilio (electricistas, gasistas, plomeros). Gestiona perfiles de profesionales, catálogo de tarifas por rubro y reserva de turnos con control de concurrencia.

> **Estado:** En desarrollo activo. Modelo de dominio y arquitectura de base de datos inicial configurados.

---

## Stack Tecnológico

- **Lenguaje:** Java 21
- **Framework:** Spring Boot 4.1.x (Spring Web, Spring Data JPA, Spring Security, Validation)
- **Base de Datos:** PostgreSQL
- **Seguridad:** Autenticación y autorización sin estado con JWT (Control de acceso basado en roles - RBAC)
- **Gestor de dependencias:** Maven
- **Entorno:** Docker / Docker Compose

---

## Funcionalidades Principales

- **Control de acceso por roles (RBAC):** Separación estricta de permisos para `CUSTOMER`, `TECHNICIAN` y `ADMIN`.
- **Catálogo de técnicos y tarifas:** Configuración de costo base de inspección/visita, categorías de servicio y zonas de cobertura.
- **Motor de turnos y agenda:** Generación dinámica de franjas horarias según la disponibilidad semanal del técnico, respaldada por aislamiento transaccional (`@Transactional`) para prevenir reservas solapadas (*double-booking*).
- **Sistema de reseñas:** Calificaciones y comentarios asociados exclusivamente a turnos completados y verificados.

---

## Arquitectura y Estructura del Proyecto

Arquitectura en capas basada en principios SOLID y separación clara de responsabilidades:

```text
src/main/java/com/flowerservices/
 ├── config/          # Filtros de seguridad, proveedor JWT, CORS
 ├── controller/      # Endpoints REST
 ├── dto/             # Objetos de transferencia de datos (Request/Response)
 ├── entity/          # Entidades JPA (User, Technician, Booking, etc.)
 ├── enums/           # Enums de dominio (Role, BookingStatus)
 ├── exception/       # Manejo global de excepciones (@RestControllerAdvice)
 ├── repository/      # Interfaces de Spring Data JPA
 └── service/         # Lógica de negocio y límites transaccionales
```

---

## Modelo de Datos

El esquema relacional está estructurado sobre las siguientes entidades clave:

- **Usuarios y Perfiles:** `User`, `Customer`, `Technician` (extensión uno a uno de las credenciales base).
- **Servicios:** `Service` y `TechnicianService` (relación N:M que define precios y descripciones por técnico).
- **Disponibilidad y Turnos:** `Availability` (franjas horarias recurrentes) y `Booking` (máquina de estados: `PENDING`, `CONFIRMED`, `COMPLETED`, `CANCELLED`).
- **Calificaciones:** `Review` (restricción uno a uno por cada servicio completado).

---

## Configuración y Ejecución Local

### Requisitos previos

- Java Development Kit (JDK) 21
- Docker y Docker Compose (para la base de datos local)
- Git

### Pasos para levantar el entorno

1. Clonar el repositorio:
```bash
git clone https://github.com/Guabit/flowerservices.git
cd flowerservices
```

2. Iniciar la base de datos (PostgreSQL):
```bash
docker compose up -d
```

3. Ejecutar el backend:
```bash
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`.
