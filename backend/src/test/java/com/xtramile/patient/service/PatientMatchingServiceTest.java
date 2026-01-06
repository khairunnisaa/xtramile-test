package com.xtramile.patient.service.matching;

import com.xtramile.patient.domain.IdentifierType;
import com.xtramile.patient.domain.Patient;
import com.xtramile.patient.domain.PatientIdentifier;
import com.xtramile.patient.repository.PatientIdentifierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientMatchingServiceTest {

    @Mock
    private PatientIdentifierRepository identifierRepository;

    @InjectMocks
    private PatientMatchingService matchingService;

    private Patient existingPatient;

    @BeforeEach
    void setUp() {
        existingPatient = Patient.create(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "MALE",
                "081234567890"
        );
        
        PatientIdentifier phoneId = PatientIdentifier.create(
                existingPatient,
                IdentifierType.PHONE,
                "081234567890"
        );
        
        existingPatient.addIdentifier(phoneId);
    }

    @Test
    void testMatch_AutoMatch() {
        IncomingPatientRecord incoming = new IncomingPatientRecord(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "081234567890",
                "john@example.com"
        );
        
        List<PatientIdentifier> identifiers = new ArrayList<>();
        PatientIdentifier phoneId = PatientIdentifier.create(
                existingPatient,
                IdentifierType.PHONE,
                "081234567890"
        );
        identifiers.add(phoneId);
        
        when(identifierRepository.findAllByTypeAndNormalizedValue(
                eq(IdentifierType.PHONE), anyString())).thenReturn(identifiers);
        
        MatchDecision decision = matchingService.match(incoming, existingPatient);
        
        assertEquals(MatchDecision.AUTO_MATCH, decision);
    }

    @Test
    void testMatch_Review() {
        IncomingPatientRecord incoming = new IncomingPatientRecord(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "081999999999",
                "different@example.com"
        );
        
        when(identifierRepository.findAllByTypeAndNormalizedValue(
                any(IdentifierType.class), anyString())).thenReturn(List.of());
        
        MatchDecision decision = matchingService.match(incoming, existingPatient);
        
        assertEquals(MatchDecision.REVIEW, decision);
    }

    @Test
    void testMatch_NoMatch() {
        IncomingPatientRecord incoming = new IncomingPatientRecord(
                "Jane",
                "Smith",
                LocalDate.of(1995, 5, 5),
                "081999999999",
                "jane@example.com"
        );
        
        when(identifierRepository.findAllByTypeAndNormalizedValue(
                any(IdentifierType.class), anyString())).thenReturn(List.of());
        
        MatchDecision decision = matchingService.match(incoming, existingPatient);
        
        assertEquals(MatchDecision.NO_MATCH, decision);
    }
}
