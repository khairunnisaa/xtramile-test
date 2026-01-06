package com.xtramile.patient.controller;

import com.xtramile.patient.domain.Patient;
import com.xtramile.patient.dto.PatientRequest;
import com.xtramile.patient.dto.PatientResponse;
import com.xtramile.patient.mapper.PatientMapper;
import com.xtramile.patient.repository.PatientRepository;
import com.xtramile.patient.service.PatientService;
import com.xtramile.patient.service.matching.IncomingPatientRecord;
import com.xtramile.patient.service.matching.MatchDecision;
import com.xtramile.patient.service.matching.PatientMatchingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final PatientMatchingService matchingService;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @PostMapping
    public ResponseEntity<PatientResponse> create(@Valid @RequestBody PatientRequest request) {
        PatientResponse created = patientService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getById(@PathVariable UUID id) {
        PatientResponse patient = patientService.getById(id);
        return ResponseEntity.ok(patient);
    }

    @GetMapping
    public ResponseEntity<Page<PatientResponse>> findAll(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Page<PatientResponse> patients = patientService.findAll(pageable);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PatientResponse>> search(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<PatientResponse> patients = patientService.searchByName(name, pageable);
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody PatientRequest request) {
        PatientResponse updated = patientService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/match")
    public ResponseEntity<MatchResult> match(@Valid @RequestBody MatchRequest request) {
        IncomingPatientRecord incoming = new IncomingPatientRecord(
                request.getFirstName(),
                request.getLastName(),
                request.getDateOfBirth(),
                request.getPhone(),
                request.getEmail()
        );

        Patient bestMatch = null;
        MatchDecision decision = MatchDecision.NO_MATCH;

        for (Patient patient : patientRepository.findAll()) {
            MatchDecision currentDecision = matchingService.match(incoming, patient);
            if (currentDecision == MatchDecision.AUTO_MATCH) {
                bestMatch = patient;
                decision = currentDecision;
                break;
            } else if (currentDecision == MatchDecision.REVIEW && bestMatch == null) {
                bestMatch = patient;
                decision = currentDecision;
            }
        }

        MatchResult result = MatchResult.builder()
                .decision(decision.name())
                .score(decision == MatchDecision.AUTO_MATCH ? 3 : decision == MatchDecision.REVIEW ? 2 : 0)
                .build();

        if (bestMatch != null) {
            result.setMatchedPatient(patientMapper.toResponse(bestMatch));
        }

        return ResponseEntity.ok(result);
    }

    @lombok.Data
    @lombok.Builder
    public static class MatchRequest {
        private String firstName;
        private String lastName;
        private java.time.LocalDate dateOfBirth;
        private String phone;
        private String email;
    }

    @lombok.Data
    @lombok.Builder
    public static class MatchResult {
        private String decision;
        private PatientResponse matchedPatient;
        private int score;
    }
}
