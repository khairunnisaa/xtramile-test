package com.xtramile.patient.repository;

import com.xtramile.patient.domain.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void testSaveAndFindPatient() {
        Patient patient = Patient.create(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "MALE",
                "081234567890"
        );
        
        Patient saved = patientRepository.save(patient);
        
        assertNotNull(saved.getId());
        assertTrue(patientRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void testSearchByName() {
        Patient patient = Patient.create(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "MALE",
                "081234567890"
        );
        patientRepository.save(patient);
        
        Page<Patient> results = patientRepository.searchByName("John", PageRequest.of(0, 10));
        
        assertTrue(results.getTotalElements() > 0);
    }
}
