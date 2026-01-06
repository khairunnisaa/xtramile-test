# Entity Relationship Diagram

## Entities

### Patient
- **patient_id** (PK, UUID)
- first_name
- last_name
- dob
- gender
- created_at

### PatientIdentifier
- **identifier_id** (PK, UUID)
- patient_id (FK)
- type (MRN, NATIONAL_ID, PHONE, EMAIL)
- value
- normalized_value
- Unique constraint: (type, normalized_value)

### PatientLink
- **link_id** (PK, UUID)
- patient_id_a (FK)
- patient_id_b (FK)
- confidence_level
- created_at

### Address
- **address_id** (PK, UUID)
- patient_id (FK)
- address
- suburb
- state
- postcode

## Relationships
- Patient 1:N PatientIdentifier
- Patient 1:N Address
- Patient N:N PatientLink (self-referential)

