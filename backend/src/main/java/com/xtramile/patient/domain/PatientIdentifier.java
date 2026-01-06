package com.xtramile.patient.domain;

import com.xtramile.patient.util.NormalizationUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(
    name = "patient_identifiers",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"type", "normalized_value"})
    },
    indexes = {
        @Index(name = "idx_identifier_normalized", columnList = "normalized_value")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatientIdentifier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "identifier_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IdentifierType type;

    @Column(name = "identifier_value", nullable = false)
    private String value;

    @Column(name = "normalized_value", nullable = false)
    private String normalizedValue;

    private PatientIdentifier(
            Patient patient,
            IdentifierType type,
            String value
    ) {
        this.patient = patient;
        this.type = type;
        this.value = value;
        this.normalizedValue = normalize(type, value);
    }

    public static PatientIdentifier create(
            Patient patient,
            IdentifierType type,
            String value
    ) {
        return new PatientIdentifier(patient, type, value);
    }

    private String normalize(IdentifierType type, String value) {
        if (value == null) return "";

        return switch (type) {
            case EMAIL -> NormalizationUtils.normalizeEmail(value);
            case PHONE -> NormalizationUtils.normalizePhoneForMatching(value);
            case NATIONAL_ID, MRN -> NormalizationUtils.normalizeForMatching(value);
        };
    }
}
