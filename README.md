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
│   └── src/main/java/com/xtramile/algorithm/  # Basic algorithms
│       ├── UniqueNumbersFinder.java          # Find unique numbers
│       └── MaxSumFinder.java                 # Largest sum of two elements
├── frontend/         # Angular application
├── docs/            # Architecture and design docs
└── .github/         # CI/CD workflows
```

## Algorithms

### Basic Algorithms

Basic algorithm implementations are located in `backend/src/main/java/com/xtramile/algorithm/`:

1. **UniqueNumbersFinder** - Find numbers that occur exactly once
   - Time complexity: O(n)
   - Space complexity: O(n)
   - Location: `backend/src/main/java/com/xtramile/algorithm/UniqueNumbersFinder.java`

2. **MaxSumFinder** - Find largest sum of any two elements
   - Time complexity: O(n)
   - Space complexity: O(1)
   - Location: `backend/src/main/java/com/xtramile/algorithm/MaxSumFinder.java`

### Patient Matching Algorithm

The patient matching algorithm determines if an incoming patient record matches an existing patient.

**Requirements:**
- Minimum 2 out of 4 fields matching: (name, dob, phone, email)
- Normalize fields (lowercase email, remove spaces in phone)
- Output: AUTO_MATCH, REVIEW, or NO_MATCH

**Implementation:**
- Location: `backend/src/main/java/com/xtramile/patient/service/matching/PatientMatchingService.java`
- Documentation: `docs/matching-algorithm.md`
- API Endpoint: `POST /api/patients/match`

**Matching Rules:**
- **AUTO_MATCH**: Strong identifier match (PHONE/EMAIL) OR 3+ demographic fields match
- **REVIEW**: Exactly 2 demographic fields match
- **NO_MATCH**: Less than 2 demographic fields match

See `docs/matching-algorithm.md` for detailed pseudocode and sample inputs/outputs.

### Running Algorithm Tests
```bash
cd backend
mvn test -Dtest=UniqueNumbersFinderTest,MaxSumFinderTest
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
npm start
```

Or using Angular CLI directly:
```bash
cd frontend
npm install
ng serve
```

Frontend runs on `http://localhost:4200`

**Note:** Make sure backend is running on `http://localhost:8080` for API calls to work.

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

