package com.xtramile.patient.service.matching;

import lombok.Value;

import java.time.LocalDate;

@Value
public class IncomingPatientRecord {
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String phone;
    String email;
}

