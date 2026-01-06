# Patient MPI Backend

Spring Boot backend application for Patient Master Patient Index system.

## Tech Stack
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (in-memory)
- Maven

## Building
```bash
./mvnw clean install
```

## Running
```bash
./mvnw spring-boot:run
```

## Testing
```bash
./mvnw test
```

## API Documentation
See `/docs/api-contract.md` for detailed API documentation.

## H2 Console
Access H2 console at: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:patientdb`
- Username: `sa`
- Password: (empty)

