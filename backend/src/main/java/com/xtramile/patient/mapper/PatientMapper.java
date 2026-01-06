package com.xtramile.patient.mapper;

import com.xtramile.patient.domain.Address;
import com.xtramile.patient.domain.IdentifierType;
import com.xtramile.patient.domain.Patient;
import com.xtramile.patient.domain.PatientIdentifier;
import com.xtramile.patient.dto.AddressDto;
import com.xtramile.patient.dto.PatientRequest;
import com.xtramile.patient.dto.PatientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "patientId", source = "id")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "address", expression = "java(patient.getAddresses().isEmpty() ? null : toAddressDto(patient.getAddresses().get(0)))")
    PatientResponse toResponse(Patient patient);

    default Patient toEntity(PatientRequest request) {
        Patient patient = Patient.create(
                request.getFirstName(),
                request.getLastName(),
                request.getDateOfBirth(),
                request.getGender(),
                request.getPhone(),
                request.getPid()
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

        if (request.getAddress() != null && !request.getAddress().isEmpty()) {
            Address address = Address.create(
                    patient,
                    request.getAddress().getAddress(),
                    request.getAddress().getSuburb(),
                    request.getAddress().getState(),
                    request.getAddress().getPostcode()
            );
            patient.getAddresses().add(address);
        }

        return patient;
    }

    default AddressDto toAddressDto(Address address) {
        if (address == null) return null;
        AddressDto dto = new AddressDto();
        dto.setAddress(address.getAddress());
        dto.setSuburb(address.getSuburb());
        dto.setState(address.getState());
        dto.setPostcode(address.getPostcode());
        return dto;
    }
}
