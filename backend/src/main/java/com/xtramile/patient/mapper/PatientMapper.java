package com.xtramile.patient.mapper;

import com.xtramile.patient.domain.IdentifierType;
import com.xtramile.patient.domain.Patient;
import com.xtramile.patient.domain.PatientIdentifier;
import com.xtramile.patient.dto.PatientRequest;
import com.xtramile.patient.dto.PatientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "patientId", source = "id")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    PatientResponse toResponse(Patient patient);

    default Patient toEntity(PatientRequest request) {
        Patient patient = Patient.create(
                request.getFirstName(),
                request.getLastName(),
                request.getDateOfBirth(),
                request.getGender(),
                request.getPhone()
        );

        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            PatientIdentifier phoneId = PatientIdentifier.create(
                    patient,
                    IdentifierType.PHONE,
                    request.getPhone()
            );
            patient.addIdentifier(phoneId);
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            PatientIdentifier emailId = PatientIdentifier.create(
                    patient,
                    IdentifierType.EMAIL,
                    request.getEmail()
            );
            patient.addIdentifier(emailId);
        }

        return patient;
    }
}
