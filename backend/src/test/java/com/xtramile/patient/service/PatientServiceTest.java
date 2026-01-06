package com.xtramile.patient.service;

import com.xtramile.patient.domain.Patient;
import com.xtramile.patient.dto.PatientRequest;
import com.xtramile.patient.dto.PatientResponse;
import com.xtramile.patient.mapper.PatientMapper;
import com.xtramile.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientService patientService;

    private PatientRequest patientRequest;
    private Patient patient;
    private PatientResponse patientResponse;

    @BeforeEach
    void setUp() {
        UUID patientId = UUID.randomUUID();
        
        patientRequest = new PatientRequest();
        patientRequest.setFirstName("John");
        patientRequest.setLastName("Doe");
        patientRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientRequest.setGender("MALE");
        patientRequest.setPhone("081234567890");
        
        patient = Patient.create(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "MALE",
                "081234567890",
                "12345678901"
        );
        
        patientResponse = PatientResponse.builder()
                .patientId(patientId)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender("MALE")
                .phone("081234567890")
                .build();
    }

    @Test
    void testCreatePatient() {
        when(patientMapper.toEntity(any(PatientRequest.class))).thenReturn(patient);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(patientMapper.toResponse(any(Patient.class))).thenReturn(patientResponse);

        PatientResponse result = patientService.create(patientRequest);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testGetPatient() {
        UUID patientId = UUID.randomUUID();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientMapper.toResponse(any(Patient.class))).thenReturn(patientResponse);

        PatientResponse result = patientService.getById(patientId);

        assertNotNull(result);
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void testGetPatientNotFound() {
        UUID patientId = UUID.randomUUID();
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> patientService.getById(patientId));
    }

    @Test
    void testDeletePatient() {
        UUID patientId = UUID.randomUUID();
        when(patientRepository.existsById(patientId)).thenReturn(true);

        patientService.delete(patientId);

        verify(patientRepository, times(1)).deleteById(patientId);
    }
}
