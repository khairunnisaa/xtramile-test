package com.xtramile.patient.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String email;
}

