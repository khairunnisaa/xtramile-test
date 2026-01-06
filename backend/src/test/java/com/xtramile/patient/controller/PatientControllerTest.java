package com.xtramile.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xtramile.patient.dto.PatientRequest;
import com.xtramile.patient.dto.PatientResponse;
import com.xtramile.patient.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @MockBean
    private com.xtramile.patient.service.matching.PatientMatchingService matchingService;

    @MockBean
    private com.xtramile.patient.repository.PatientRepository patientRepository;

    @MockBean
    private com.xtramile.patient.mapper.PatientMapper patientMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreatePatient() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setGender("MALE");
        request.setPhone("081234567890");
        
        PatientResponse response = PatientResponse.builder()
                .patientId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender("MALE")
                .phone("081234567890")
                .build();
        
        when(patientService.create(any(PatientRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testGetPatient() throws Exception {
        UUID patientId = UUID.randomUUID();
        PatientResponse response = PatientResponse.builder()
                .patientId(patientId)
                .firstName("John")
                .lastName("Doe")
                .build();
        
        when(patientService.getById(patientId)).thenReturn(response);

        mockMvc.perform(get("/api/patients/{id}", patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testGetAllPatients() throws Exception {
        PatientResponse response = PatientResponse.builder()
                .patientId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender("MALE")
                .phone("081234567890")
                .build();
        
        Page<PatientResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(patientService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
