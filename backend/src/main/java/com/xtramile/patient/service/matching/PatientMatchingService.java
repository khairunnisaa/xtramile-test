package com.xtramile.patient.service.matching;

import com.xtramile.patient.domain.IdentifierType;
import com.xtramile.patient.domain.Patient;
import com.xtramile.patient.domain.PatientIdentifier;
import com.xtramile.patient.repository.PatientIdentifierRepository;
import com.xtramile.patient.util.NormalizationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PatientMatchingService {

    private final PatientIdentifierRepository identifierRepository;

    public MatchDecision match(
            IncomingPatientRecord incoming,
            Patient existing
    ) {
        MatchDecision identifierDecision = matchByIdentifier(incoming, existing);
        if (identifierDecision != null) {
            return identifierDecision;
        }

        return matchByDemographics(incoming, existing);
    }

    private MatchDecision matchByIdentifier(
            IncomingPatientRecord incoming,
            Patient existing
    ) {
        if (matchStrongIdentifier(
                IdentifierType.PHONE,
                NormalizationUtils.normalizePhoneForMatching(incoming.getPhone()),
                existing
        )) {
            return MatchDecision.AUTO_MATCH;
        }

        if (matchStrongIdentifier(
                IdentifierType.EMAIL,
                NormalizationUtils.normalizeForMatching(incoming.getEmail()),
                existing
        )) {
            return MatchDecision.AUTO_MATCH;
        }

        return null;
    }

    private boolean matchStrongIdentifier(
            IdentifierType type,
            String normalizedValue,
            Patient existing
    ) {
        if (normalizedValue == null || normalizedValue.isEmpty()) {
            return false;
        }

        List<PatientIdentifier> identifiers =
                identifierRepository.findAllByTypeAndNormalizedValue(
                        type,
                        normalizedValue
                );

        if (identifiers.isEmpty()) {
            return false;
        }

        if (identifiers.size() > 1) {
            return false;
        }

        return identifiers.get(0)
                .getPatient()
                .getId()
                .equals(existing.getId());
    }

    private MatchDecision matchByDemographics(
            IncomingPatientRecord in,
            Patient p
    ) {
        int score = 0;

        if (matchName(in, p)) score++;
        if (matchDob(in, p)) score++;
        if (matchPhone(in, p)) score++;
        if (matchEmail(in, p)) score++;

        if (score >= 3) return MatchDecision.AUTO_MATCH;
        if (score == 2) return MatchDecision.REVIEW;
        return MatchDecision.NO_MATCH;
    }

    private boolean matchName(IncomingPatientRecord in, Patient p) {
        return NormalizationUtils.normalizeForMatching(in.getFirstName())
                .equals(NormalizationUtils.normalizeForMatching(p.getFirstName()))
            && NormalizationUtils.normalizeForMatching(in.getLastName())
                .equals(NormalizationUtils.normalizeForMatching(p.getLastName()));
    }

    private boolean matchDob(IncomingPatientRecord in, Patient p) {
        return Objects.equals(in.getDateOfBirth(), p.getDateOfBirth());
    }

    private boolean matchPhone(IncomingPatientRecord in, Patient p) {
        return NormalizationUtils.normalizePhoneForMatching(in.getPhone())
                .equals(NormalizationUtils.normalizePhoneForMatching(p.getPhone()));
    }

    private boolean matchEmail(IncomingPatientRecord in, Patient p) {
        String incomingEmail = NormalizationUtils.normalizeForMatching(in.getEmail());
        if (incomingEmail.isEmpty()) {
            return false;
        }
        
        return p.getIdentifiers().stream()
                .filter(id -> id.getType() == IdentifierType.EMAIL)
                .anyMatch(id -> incomingEmail.equals(id.getNormalizedValue()));
    }
}

