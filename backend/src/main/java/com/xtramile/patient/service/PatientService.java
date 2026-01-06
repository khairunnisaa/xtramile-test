package com.xtramile.patient.service;

import com.xtramile.patient.domain.Patient;
import com.xtramile.patient.dto.PatientRequest;
import com.xtramile.patient.dto.PatientResponse;
import com.xtramile.patient.mapper.PatientMapper;
import com.xtramile.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository repository;
    private final PatientMapper mapper;

    @Transactional
    public PatientResponse create(PatientRequest request) {
        Patient patient = mapper.toEntity(request);
        Patient saved = repository.save(patient);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PatientResponse getById(UUID id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return mapper.toResponse(patient);
    }

    @Transactional(readOnly = true)
    public Page<PatientResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<PatientResponse> searchByName(String name, Pageable pageable) {
        return repository.searchByName(name, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public PatientResponse update(UUID id, PatientRequest request) {
        Patient existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        existing.updateName(request.getFirstName(), request.getLastName());
        if (request.getPhone() != null) {
            existing.updateContact(request.getPhone());
        }

        Patient updated = repository.save(existing);
        return mapper.toResponse(updated);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Patient not found");
        }
        repository.deleteById(id);
    }
}
