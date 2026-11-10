# Enterprise Service Desk Management System

A role-based IT service desk backend built with **Java, Spring Boot, Spring Security, JWT, and MySQL**. The application provides REST APIs for employees to raise support tickets, managers to assign and prioritize them, support engineers to resolve them, and administrators to manage users.

> **Project scope:** This repository contains the backend REST API. A frontend application is not included.

<p align="left">
  <img src="https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white" alt="Java 17" />
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot 4.0.6" />
  <img src="https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?logo=springsecurity&logoColor=white" alt="Spring Security and JWT" />
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white" alt="MySQL 8" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white" alt="Docker Compose" />
  <img src="https://img.shields.io/badge/API-REST-informational" alt="REST API" />
</p>

## Table of Contents

- [Overview](#overview)
- [Core Features](#core-features)
- [User Roles](#user-roles)
- [Ticket Lifecycle](#ticket-lifecycle)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Data Model](#data-model)
- [Getting Started](#getting-started)
- [API Reference](#api-reference)
- [Authentication](#authentication)
- [Example API Workflow](#example-api-workflow)
- [Project Structure](#project-structure)
- [Running Tests](#running-tests)
- [Configuration and Security Notes](#configuration-and-security-notes)
- [Future Improvements](#future-improvements)

## Overview

The Enterprise Service Desk Management System is designed to organize an organization's internal IT support process. Employees can report technical issues through tickets instead of handling requests informally. Managers can review incoming tickets, assign them to support engineers, and adjust their priority. Support engineers can track the tickets assigned to them and update their progress. Administrators can manage user roles and account statuses.

The backend follows a layered Spring Boot structure, separating API controllers, business services, data access, entities, and security components.

## Core Features

- **JWT authentication:** Register and log in through REST endpoints; authenticated requests use a Bearer token.
- **Role-based authorization:** Protects employee, manager, support engineer, and administrator API routes.
- **Ticket management:** Create tickets, view relevant tickets, assign tickets, update priority, and manage ticket status.
- **Assignment workflow:** Managers assign open tickets to users with the support engineer role.
- **Status transition rules:** Support engineers move assigned tickets through progress and resolution; the employee who created a ticket can close it after resolution.
- **User administration:** Administrators can view users, change roles, and activate or deactivate accounts.
- **Dashboard endpoints:** Separate dashboard APIs are provided for the employee, manager, support engineer, and administrator roles.
- **API documentation:** Swagger UI and OpenAPI documentation are available through Springdoc.
- **Containerization:** Docker and Docker Compose are configured to build and run the application with MySQL.

## User Roles

| Role | Responsibilities | API base path |
|---|---|---|
| Employee | Register, log in, create tickets, view own tickets, view a ticket, and close a resolved ticket | /api/tickets |
| Manager | View tickets, assign support engineers, change ticket priority, and view ticket statistics | /api/manager |
| Support Engineer | View assigned tickets, update ticket status, and view assigned-ticket statistics | /api/support |
| Admin | View users, update user roles and account statuses, and view system statistics | /api/admin |

New registrations are assigned the **EMPLOYEE** role and **ACTIVE** status by the application. The registration endpoint does not allow a client to choose an elevated role. Manager, support engineer, and admin accounts must be provisioned through an authorized administrative process.

## Ticket Lifecycle

A ticket follows the workflow below:

~~~mermaid
flowchart TD
    A[Employee creates ticket] --> B[OPEN]
    B --> C[Manager assigns support engineer]
    C --> D[ASSIGNED]
    D --> E[Support engineer starts work]
    E --> F[IN_PROGRESS]
    F --> G[Support engineer resolves issue]
    G --> H[RESOLVED]
    H --> I[Employee closes ticket]
    I --> J[CLOSED]
~~~

Ticket defaults and rules implemented in the current service:

- Newly created tickets start with **OPEN** status and **MEDIUM** priority.
- A manager can assign a ticket to a user whose role is **SUPPORT_ENGINEER**.
- A support engineer can change an assigned ticket from **ASSIGNED** to **IN_PROGRESS**, and then from **IN_PROGRESS** to **RESOLVED**.
- The employee who created the ticket can close it after it reaches **RESOLVED**.
- Ticket priority can be changed while the ticket is not **CLOSED**.

Available ticket statuses:

| Status | Meaning |
|---|---|
| OPEN | Ticket has been created and is awaiting assignment |
| ASSIGNED | A support engineer has been assigned |
| IN_PROGRESS | The support engineer is working on the issue |
| RESOLVED | The reported issue has been resolved |
| CLOSED | The employee has closed the resolved ticket |

Available priorities: **LOW**, **MEDIUM**, **HIGH**, and **CRITICAL**.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Application language and compilation target |
| Spring Boot 4.0.6 | Application framework |
| Spring WebMVC | REST API endpoints |
| Spring Data JPA / Hibernate | ORM and database access |
| Spring Security | Authentication and authorization |
| JSON Web Token (JJWT 0.12.6) | Token generation and validation |
| BCrypt | Password hashing |
| MySQL 8.0 | Relational database |
| Maven Wrapper | Build and dependency management |
| Springdoc OpenAPI 2.8.9 | Swagger UI and API documentation |
| Docker / Docker Compose | Containerized application and database |

The Dockerfile uses Eclipse Temurin 21 for its build and runtime images, while the Maven project is configured with Java 17 as its compilation target.

## Architecture

The application uses a layered architecture:

~~~mermaid
flowchart TB
    Client[Client / Postman / Swagger UI]
    Security[Spring Security Filter Chain]
    JWT[JWT Authentication Filter]
    Controllers[REST Controllers]
    Services[Service Layer]
    Repositories[Spring Data JPA Repositories]
    Database[(MySQL Database)]

    Client --> Security
    Security --> JWT
    JWT --> Controllers
    Controllers --> Services
    Services --> Repositories
    Repositories --> Database
~~~

**Layer responsibilities**

- **Controller:** Receives HTTP requests, reads path variables and request bodies, and returns API responses.
- **Service:** Implements business rules for authentication, user management, ticket assignment, and ticket status transitions.
- **Repository:** Uses Spring Data JPA to access persisted users and tickets.
- **Entity:** Represents the database models, primarily User and Ticket.
- **Security:** Validates JWTs, loads the associated user, and applies role-based access rules.
- **DTO:** Defines request and response payloads so API clients do not need to work directly with JPA entities.

## Data Model

The current application has two main entities.

### User

Stores account information:

- User ID
- User name
- Email address (unique)
- BCrypt-encoded password
- Role
- Account status

### Ticket

Stores support request information:

- Ticket ID
- Title and description
- Status and priority
- Creation timestamp
- User who created the ticket
- Support engineer assigned to the ticket, when assigned

### Relationships

- A user can create multiple tickets. Each ticket has one creator through the createdBy relationship.
- A user with the support engineer role can be assigned multiple tickets. Each ticket can have zero or one assigned support engineer through the assignedTo relationship.

These are many-to-one relationships from Ticket to User and are represented by the created_by and assigned_to foreign-key columns.

## Getting Started

### Prerequisites

For the Docker-based setup:

- Docker Desktop, or Docker Engine with the Docker Compose plugin
- Git

For running the application directly on your machine:

- JDK 17 or compatible
- MySQL 8.0
- Git

### 1. Clone the repository

~~~bash
git clone https://github.com/MANI-07-code/Enterprise-Service-Desk-Management-System.git
cd Enterprise-Service-Desk-Management-System
~~~

### 2. Start the application and database

The repository includes a multi-stage Dockerfile and a Docker Compose configuration.

~~~bash
docker compose up --build -d
~~~

Check the running containers:

~~~bash
docker compose ps
~~~

View application logs:

~~~bash
docker compose logs -f app
~~~

The API is exposed on port **8080**.

### 3. Open the API documentation

Once the application has started, open:

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

You can use Swagger UI or a tool such as Postman to send requests.

### 4. Stop the application

Stop and remove the containers:

~~~bash
docker compose down
~~~

The MySQL data is stored in the named Docker volume **mysql_data**, so stopping the containers does not normally remove the database.

To remove the containers and the database volume as well:

~~~bash
docker compose down -v
~~~

> **Warning:** The `-v` option deletes the persisted MySQL data for this Compose project.

### Running outside Docker

The checked-in datasource URL uses the Docker Compose service hostname `mysql`. If you run Spring Boot directly on your host machine, configure the datasource URL for your local MySQL instance (commonly `jdbc:mysql://localhost:3306/helpdesk_db`) and provide matching database credentials before starting the application with the Maven Wrapper.

## API Reference

All protected endpoints require a valid JWT in the Authorization header. The authentication and API documentation routes are public.

### Authentication

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | /api/auth/register | Public | Register an employee account |
| POST | /api/auth/login | Public | Authenticate and receive a JWT |

### Employee

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | /api/tickets | EMPLOYEE | Create a ticket |
| GET | /api/tickets/my | EMPLOYEE | List tickets created by the logged-in employee |
| GET | /api/tickets/{id} | EMPLOYEE | Get a ticket by ID, subject to ownership checks |
| PUT | /api/tickets/{id}/close | EMPLOYEE | Close the employee's resolved ticket |
| GET | /api/tickets/dashboard | EMPLOYEE | Get the employee dashboard response |

### Manager

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | /api/manager/tickets | MANAGER | List all tickets |
| PUT | /api/manager/tickets/{id}/assign | MANAGER | Assign a ticket to a support engineer |
| PUT | /api/manager/tickets/{id}/priority | MANAGER | Change ticket priority |
| GET | /api/manager/dashboard | MANAGER | Get ticket status counts |

### Support Engineer

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | /api/support/tickets | SUPPORT_ENGINEER | List tickets assigned to the logged-in engineer |
| PUT | /api/support/tickets/{id}/status | SUPPORT_ENGINEER | Update the status of an assigned ticket |
| GET | /api/support/dashoard | SUPPORT_ENGINEER | Get assigned-ticket status counts |

**Note:** The support dashboard route is currently spelled **dashoard** in the controller. The API table reflects the route implemented in the source code.

### Admin

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | /api/admin/users | ADMIN | List all users |
| GET | /api/admin/users/{id} | ADMIN | Get a user by ID |
| PUT | /api/admin/users/{id}/role | ADMIN | Change a user's role |
| PUT | /api/admin/users/{id}/status | ADMIN | Activate or deactivate a user |
| GET | /api/admin/dashboard | ADMIN | Get user and ticket statistics |

### API documentation

| Resource | Endpoint |
|---|---|
| Swagger UI | /swagger-ui/index.html |
| OpenAPI specification | /v3/api-docs |

## Authentication

The application uses stateless JWT authentication.

1. A user registers through the registration endpoint.
2. The user logs in with their email and password.
3. The application verifies the password and returns a signed JWT.
4. The client includes the token in the Authorization header for protected requests.
5. The JWT filter validates the token, loads the user, and establishes the authenticated security context.
6. Spring Security checks the user's current role against the endpoint's required authority.

Example Authorization header:

~~~http
Authorization: Bearer <your-jwt-token>
~~~

The current implementation sets JWT expiration to **24 hours**. Passwords are encoded with BCrypt before being stored.

## Example API Workflow

The following examples demonstrate the main employee ticket workflow. Replace the example email, password, IDs, and token with values from your own local environment.

### 1. Register an employee

~~~bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "Alex",
    "email": "alex@example.com",
    "password": "ChangeMe123"
  }'
~~~

The application creates the account with the EMPLOYEE role and ACTIVE status.

### 2. Log in

~~~bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alex@example.com",
    "password": "ChangeMe123"
  }'
~~~

The response contains a JWT token along with the user's basic account details. Copy the token for the next request.

### 3. Create a ticket

~~~bash
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <employee-token>" \
  -d '{
    "title": "Unable to connect to office network",
    "description": "My laptop cannot connect to the office Wi-Fi."
  }'
~~~

The service assigns the initial status **OPEN** and priority **MEDIUM**.

### 4. Manager assigns a support engineer

A manager sends the support engineer's user ID:

~~~bash
curl -X PUT http://localhost:8080/api/manager/tickets/1/assign \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <manager-token>" \
  -d '{
    "supportEngineerId": 3
  }'
~~~

### 5. Support engineer updates the ticket

Move the ticket to **IN_PROGRESS**:

~~~bash
curl -X PUT http://localhost:8080/api/support/tickets/1/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <support-engineer-token>" \
  -d '{
    "status": "IN_PROGRESS"
  }'
~~~

After resolving the issue, update it to **RESOLVED**:

~~~bash
curl -X PUT http://localhost:8080/api/support/tickets/1/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <support-engineer-token>" \
  -d '{
    "status": "RESOLVED"
  }'
~~~

### 6. Employee closes the resolved ticket

~~~bash
curl -X PUT http://localhost:8080/api/tickets/1/close \
  -H "Authorization: Bearer <employee-token>"
~~~

The ticket is now **CLOSED**.

## Project Structure

~~~text
src/
└── main/
    ├── java/com/mani/mainproject/
    │   ├── config/          # Spring Security and OpenAPI configuration
    │   ├── controller/      # REST API controllers
    │   ├── dto/             # Request and response DTOs
    │   ├── entity/          # JPA entities: User and Ticket
    │   ├── enums/           # Roles, statuses, and priorities
    │   ├── exception/       # Custom exceptions and global exception handling
    │   ├── repository/      # Spring Data JPA repositories
    │   ├── security/        # JWT service and authentication filter
    │   ├── service/         # Service interfaces and implementations
    │   └── MainProjectApplication.java
    └── resources/
        └── application.properties

Dockerfile
docker-compose.yml
pom.xml
mvnw
mvnw.cmd
~~~

## Running Tests

Run the tests with the Maven Wrapper.

On Linux or macOS:

~~~bash
./mvnw test
~~~

On Windows:

~~~bat
mvnw.cmd test
~~~

## Configuration and Security Notes

**Review these items before deploying this project or exposing it to a public network:**

- The current repository contains hard-coded MySQL credentials in the application and Docker Compose configuration. Replace them with environment-based configuration or a secrets manager, and rotate the committed credential.
- The JWT signing key is hard-coded in the JWT service. Replace it with a strong, externally managed secret and rotate the committed key. Do not reuse the development signing key in another environment.
- The current JPA configuration uses `spring.jpa.hibernate.ddl-auto=update` and enables SQL logging. Review these settings for production; use a controlled database migration strategy and appropriate logging.
- The Compose setup is intended for local development. Add database health checks, stronger database users and permissions, network restrictions, and production deployment configuration before treating it as production-ready.
- Provision the first administrator through a controlled process. Public registration creates employee accounts only.

Do not commit passwords, signing keys, access tokens, or other secrets to source control.

## Future Improvements

Potential next steps for the project include:

- Externalize database credentials and JWT signing keys.
- Add database migrations with Flyway or Liquibase.
- Add comprehensive unit, integration, and API security tests.
- Add pagination, filtering, and sorting for ticket lists.
- Add ticket comments, attachments, and audit history.
- Add notifications for assignment and ticket status changes.
- Add a frontend dashboard for each user role.
- Add CI/CD automation and production deployment configuration.

---

## Author

**Manikandan**

- GitHub: [MANI-07-code](https://github.com/MANI-07-code)
- Project: [Enterprise Service Desk Management System](https://github.com/MANI-07-code/Enterprise-Service-Desk-Management-System)

Contributions, suggestions, and feedback are welcome.
