# Architecture Overview

## High-Level Design

The Patient MPI (Master Patient Index) system is designed to manage patient records with identifier-based matching capabilities.

## System Components

### Backend (Spring Boot)
- **Domain Layer**: JPA entities representing core business objects
- **Repository Layer**: Data access using Spring Data JPA
- **Service Layer**: Business logic including matching algorithm
- **Controller Layer**: REST API endpoints
- **DTO Layer**: API contract boundaries
- **Mapper Layer**: Entity ↔ DTO transformations

### Frontend (Angular)
- **Core Module**: Interceptors, API services, guards
- **Features Module**: Patient management features
- **Shared Module**: Reusable components

## Design Decisions

### Why JPA?
- Standard ORM for Java/Spring ecosystem
- Type-safe queries with Spring Data JPA
- Easy migration to production databases (PostgreSQL, MySQL)

### Why Server-Side Pagination?
- Scalability: Handles large datasets efficiently
- Performance: Only loads required data
- Standard practice for production healthcare systems

### Matching Algorithm
- Normalized identifier comparison
- Minimum 2 matches required from 4 fields (name, dob, phone, email)
- Confidence levels: AUTO_MATCH (3+), REVIEW (2), NO_MATCH (<2)
- Strong identifiers (phone + email) increase confidence

### Data Model
- **Patient**: Core entity with basic demographics
- **PatientIdentifier**: Separate table for identifiers (MRN, NATIONAL_ID, PHONE, EMAIL)
- **PatientLink**: Links between potentially duplicate patients
- **Address**: Patient address information

### Uniqueness Strategy
- Uniqueness enforced at identifier level, not patient level
- Normalized values stored for fast matching
- PatientLink allows merge/soft-link without destructive updates

## Technology Choices

- **H2 Database**: In-memory for development/testing, easy migration to production DB
- **Spring Boot**: Rapid development, production-ready defaults
- **Angular Material**: Professional UI components
- **Reactive Forms**: Better validation and user experience

