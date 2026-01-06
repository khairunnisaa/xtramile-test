package com.xtramile.patient.repository;

import com.xtramile.patient.domain.IdentifierType;
import com.xtramile.patient.domain.PatientIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientIdentifierRepository
        extends JpaRepository<PatientIdentifier, UUID> {

    Optional<PatientIdentifier> findByTypeAndNormalizedValue(
            IdentifierType type,
            String normalizedValue
    );

    List<PatientIdentifier> findAllByTypeAndNormalizedValue(
            IdentifierType type,
            String normalizedValue
    );

    List<PatientIdentifier> findAllByNormalizedValue(String normalizedValue);
}
