# API Contract

## Base URL
```
http://localhost:8080/api
```

## Endpoints

### Create Patient
**POST** `/patients`

**Request:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dob": "1990-01-01",
  "gender": "MALE",
  "identifiers": [
    {
      "type": "PHONE",
      "value": "081234567890"
    },
    {
      "type": "EMAIL",
      "value": "john.doe@example.com"
    }
  ],
  "address": {
    "address": "123 Main St",
    "suburb": "Downtown",
    "state": "Jakarta",
    "postcode": "10110"
  }
}
```

**Response:** `201 Created`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "firstName": "John",
  "lastName": "Doe",
  "dob": "1990-01-01",
  "gender": "MALE",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### Get Patient
**GET** `/patients/{id}`

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "firstName": "John",
  "lastName": "Doe",
  "dob": "1990-01-01",
  "gender": "MALE",
  "identifiers": [...],
  "address": {...},
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### List Patients
**GET** `/patients?page=0&size=20&sort=createdAt,desc`

**Response:** `200 OK`
```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

### Update Patient
**PUT** `/patients/{id}`

**Request:** Same as Create Patient

**Response:** `200 OK` (updated patient)

### Delete Patient
**DELETE** `/patients/{id}`

**Response:** `204 No Content`

### Search Patients
**GET** `/patients/search?name=John&pid=12345`

**Response:** `200 OK`
```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "totalElements": 5
}
```

### Match Patient
**POST** `/patients/match`

**Request:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dob": "1990-01-01",
  "phone": "081234567890",
  "email": "john.doe@example.com"
}
```

**Response:** `200 OK`
```json
{
  "decision": "AUTO_MATCH",
  "matchedPatient": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "firstName": "John",
    "lastName": "Doe"
  },
  "score": 3,
  "matchedFields": ["name", "dob", "phone"]
}
```

## Error Responses

**400 Bad Request**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [...]
}
```

**404 Not Found**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Patient not found"
}
```

