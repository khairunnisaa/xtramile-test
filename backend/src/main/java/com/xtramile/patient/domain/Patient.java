package com.xtramile.patient.domain;

import com.xtramile.patient.util.NormalizationUtils;
import com.xtramile.patient.util.PidGenerator;
import com.xtramile.patient.util.PidValidator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "patients")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Patient {

    @Id
    @Column(name = "patient_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "dob", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "pid")
    private String pid;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientIdentifier> identifiers = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.pid == null || this.pid.isEmpty()) {
            this.pid = PidGenerator.generate();
        }
    }

    protected Patient(
            UUID id,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String gender,
            String phone,
            String pid
    ) {
        this.id = id;
        this.firstName = NormalizationUtils.normalize(firstName);
        this.lastName = NormalizationUtils.normalize(lastName);
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = NormalizationUtils.normalizePhone(phone);
        this.pid = formatPid(pid);
    }

    public static Patient create(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String gender,
            String phone,
            String pid
    ) {
        String generatedPid = pid;
        if (generatedPid == null || generatedPid.isEmpty()) {
            generatedPid = PidGenerator.generate();
        }
        return new Patient(
                UUID.randomUUID(),
                firstName,
                lastName,
                dateOfBirth,
                gender,
                phone,
                generatedPid
        );
    }

    public void updateContact(String phone) {
        this.phone = NormalizationUtils.normalizePhone(phone);
    }

    public void updateName(String firstName, String lastName) {
        this.firstName = NormalizationUtils.normalize(firstName);
        this.lastName = NormalizationUtils.normalize(lastName);
    }

    public void addIdentifier(PatientIdentifier identifier) {
        this.identifiers.add(identifier);
    }

    public void ensurePid() {
        if (this.pid == null || this.pid.isEmpty()) {
            this.pid = PidGenerator.generate();
        }
    }

    private String formatPid(String pid) {
        if (pid == null || pid.isEmpty()) {
            return PidGenerator.generate();
        }
        return PidValidator.format(pid);
    }
}
