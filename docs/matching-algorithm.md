# Patient Matching Algorithm

## Overview
The matching algorithm determines if an incoming patient record matches an existing patient in the system. It uses a two-phase approach: identifier-based matching (strong match) and demographic-based matching (fuzzy match).

## Algorithm Pseudocode

```
FUNCTION match(incoming, existing):
    // Phase 1: Strong Identifier Matching (PHONE or EMAIL)
    identifierDecision = matchByIdentifier(incoming, existing)
    IF identifierDecision IS NOT NULL:
        RETURN identifierDecision
    
    // Phase 2: Demographic Matching
    RETURN matchByDemographics(incoming, existing)

FUNCTION matchByIdentifier(incoming, existing):
    normalizedPhone = normalizePhone(incoming.phone)
    IF normalizedPhone EXISTS AND matches existing patient's phone:
        RETURN AUTO_MATCH
    
    normalizedEmail = normalizeEmail(incoming.email)
    IF normalizedEmail EXISTS AND matches existing patient's email:
        RETURN AUTO_MATCH
    
    RETURN NULL

FUNCTION matchByDemographics(incoming, existing):
    score = 0
    
    // Check 4 fields: name, dob, phone, email
    IF matchName(incoming, existing):
        score = score + 1
    IF matchDob(incoming, existing):
        score = score + 1
    IF matchPhone(incoming, existing):
        score = score + 1
    IF matchEmail(incoming, existing):
        score = score + 1
    
    // Decision logic
    IF score >= 3:
        RETURN AUTO_MATCH
    ELSE IF score == 2:
        RETURN REVIEW
    ELSE:
        RETURN NO_MATCH

FUNCTION matchName(incoming, existing):
    normalizedIncomingFirst = normalize(incoming.firstName)
    normalizedIncomingLast = normalize(incoming.lastName)
    normalizedExistingFirst = normalize(existing.firstName)
    normalizedExistingLast = normalize(existing.lastName)
    
    RETURN (normalizedIncomingFirst == normalizedExistingFirst) 
        AND (normalizedIncomingLast == normalizedExistingLast)

FUNCTION matchDob(incoming, existing):
    RETURN incoming.dateOfBirth == existing.dateOfBirth

FUNCTION matchPhone(incoming, existing):
    normalizedIncoming = normalizePhone(incoming.phone)
    normalizedExisting = normalizePhone(existing.phone)
    RETURN normalizedIncoming == normalizedExisting

FUNCTION matchEmail(incoming, existing):
    normalizedIncoming = normalizeEmail(incoming.email)
    IF normalizedIncoming IS EMPTY:
        RETURN FALSE
    
    FOR EACH identifier IN existing.identifiers:
        IF identifier.type == EMAIL AND identifier.normalizedValue == normalizedIncoming:
            RETURN TRUE
    
    RETURN FALSE

FUNCTION normalize(value):
    IF value IS NULL:
        RETURN ""
    RETURN value.trim().toLowerCase()

FUNCTION normalizePhone(phone):
    IF phone IS NULL:
        RETURN ""
    RETURN phone.replaceAll("[^0-9]", "")  // Remove all non-digits

FUNCTION normalizeEmail(email):
    IF email IS NULL:
        RETURN null
    RETURN email.trim().toLowerCase()
```

## Matching Rules

### 1. Strong Identifier Matching (AUTO_MATCH)
- **PHONE Match**: If normalized phone numbers match exactly → AUTO_MATCH
- **EMAIL Match**: If normalized email addresses match exactly → AUTO_MATCH

### 2. Demographic Matching
- **4 Fields Checked**: Name, Date of Birth, Phone, Email
- **Normalization Applied**:
  - Name: Trim and lowercase
  - Phone: Remove all non-digit characters
  - Email: Trim and lowercase
  - DOB: Exact date match

### 3. Decision Logic
- **AUTO_MATCH**: 
  - Strong identifier match (PHONE or EMAIL), OR
  - 3 or more demographic fields match
- **REVIEW**: 
  - Exactly 2 demographic fields match
- **NO_MATCH**: 
  - Less than 2 demographic fields match

## Sample Inputs and Expected Outputs

### Sample 1: AUTO_MATCH (Strong Identifier)
**Incoming Patient:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-01-01",
  "phone": "081234567890",
  "email": "john.doe@example.com"
}
```

**Existing Patient:**
```json
{
  "patientId": "uuid-123",
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-01-01",
  "phone": "081234567890",
  "email": "john.doe@example.com"
}
```

**Expected Output:** `AUTO_MATCH`
**Reason:** Phone numbers match exactly (normalized: "081234567890")

---

### Sample 2: REVIEW (2 Fields Match)
**Incoming Patient:**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "dateOfBirth": "1985-05-15",
  "phone": "081111111111",
  "email": "jane.smith@example.com"
}
```

**Existing Patient:**
```json
{
  "patientId": "uuid-456",
  "firstName": "Jane",
  "lastName": "Smith",
  "dateOfBirth": "1985-05-15",
  "phone": "082222222222",
  "email": "different@example.com"
}
```

**Expected Output:** `REVIEW`
**Reason:** 
- ✅ Name matches (firstName: "jane", lastName: "smith")
- ✅ DOB matches (1985-05-15)
- ❌ Phone doesn't match
- ❌ Email doesn't match
- **Score: 2** → REVIEW

---

### Sample 3: AUTO_MATCH (3 Fields Match)
**Incoming Patient:**
```json
{
  "firstName": "Bob",
  "lastName": "Wilson",
  "dateOfBirth": "1992-03-20",
  "phone": "083333333333",
  "email": "bob.wilson@example.com"
}
```

**Existing Patient:**
```json
{
  "patientId": "uuid-789",
  "firstName": "Bob",
  "lastName": "Wilson",
  "dateOfBirth": "1992-03-20",
  "phone": "083333333333",
  "email": "different@example.com"
}
```

**Expected Output:** `AUTO_MATCH`
**Reason:**
- ✅ Name matches
- ✅ DOB matches
- ✅ Phone matches
- ❌ Email doesn't match
- **Score: 3** → AUTO_MATCH

---

### Sample 4: NO_MATCH (Less than 2 Fields)
**Incoming Patient:**
```json
{
  "firstName": "Alice",
  "lastName": "Brown",
  "dateOfBirth": "1995-07-10",
  "phone": "084444444444",
  "email": "alice.brown@example.com"
}
```

**Existing Patient:**
```json
{
  "patientId": "uuid-999",
  "firstName": "Alice",
  "lastName": "Brown",
  "dateOfBirth": "1990-01-01",
  "phone": "085555555555",
  "email": "different@example.com"
}
```

**Expected Output:** `NO_MATCH`
**Reason:**
- ✅ Name matches
- ❌ DOB doesn't match
- ❌ Phone doesn't match
- ❌ Email doesn't match
- **Score: 1** → NO_MATCH

---

## Implementation Location

**Java Implementation:**
- `com.xtramile.patient.service.matching.PatientMatchingService`
- `com.xtramile.patient.service.matching.IncomingPatientRecord`
- `com.xtramile.patient.service.matching.MatchDecision`
- `com.xtramile.patient.util.NormalizationUtils`

**API Endpoint:**
```
POST /api/patients/match
```

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-01-01",
  "phone": "081234567890",
  "email": "john@example.com"
}
```

**Response:**
```json
{
  "decision": "AUTO_MATCH",
  "matchedPatient": {
    "patientId": "uuid-123",
    "firstName": "John",
    "lastName": "Doe",
    ...
  },
  "score": 3
}
```

