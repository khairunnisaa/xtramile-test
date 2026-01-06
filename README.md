# Patient MPI System

Simplified Master Patient Index system built with Spring Boot and Angular.

## Features
- Patient CRUD operations
- Identifier-based patient matching
- Server-side pagination
- Matching decision: AUTO_MATCH, REVIEW, NO_MATCH

## Tech Stack
- Java 17, Spring Boot 3.x, JPA
- Angular 17+
- H2 (in-memory database)
- Maven

## Project Structure
```
patient-mpi-system/
├── backend/          # Spring Boot application
├── frontend/         # Angular application
├── docs/            # Architecture and design docs
└── .github/         # CI/CD workflows
```

## Prerequisites
- Java 17 or higher
- Maven 3.6+ (or use Maven wrapper if available)
- Node.js 18+ and npm (for frontend)

## How to Run

### Backend
```bash
cd backend
mvn spring-boot:run
```

Or if you have Maven wrapper:
```bash
cd backend
./mvnw spring-boot:run
```

Backend runs on `http://localhost:8080`

### Frontend
```bash
cd frontend
npm install
ng serve
```

Frontend runs on `http://localhost:4200`

### Using Docker
```bash
docker-compose up
```

## API Endpoints
- `POST /api/patients` - Create patient
- `GET /api/patients` - List patients (paginated)
- `GET /api/patients/{id}` - Get patient by ID
- `PUT /api/patients/{id}` - Update patient
- `DELETE /api/patients/{id}` - Delete patient
- `GET /api/patients/search` - Search patients
- `POST /api/patients/match` - Match incoming patient record

## Testing
```bash
cd backend
mvn test
```

Or with Maven wrapper:
```bash
cd backend
./mvnw test
```

