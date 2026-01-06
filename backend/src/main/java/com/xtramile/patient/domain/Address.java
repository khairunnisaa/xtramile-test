package com.xtramile.patient.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "addresses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "address")
    private String address;

    @Column(name = "suburb")
    private String suburb;

    @Column(name = "state")
    private String state;

    @Column(name = "postcode")
    private String postcode;

    protected Address(Patient patient, String address, String suburb, String state, String postcode) {
        this.patient = patient;
        this.address = address;
        this.suburb = suburb;
        this.state = state;
        this.postcode = postcode;
    }

    public static Address create(Patient patient, String address, String suburb, String state, String postcode) {
        return new Address(patient, address, suburb, state, postcode);
    }
}
