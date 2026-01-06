package com.xtramile.patient.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class PatientResponse {
    UUID patientId;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String gender;
    String phone;
    LocalDateTime createdAt;
}

