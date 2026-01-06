package com.xtramile.patient.dto;

import lombok.Data;

@Data
public class AddressDto {
    private String address;
    private String suburb;
    private String state;
    private String postcode;
    
    public boolean isEmpty() {
        return (address == null || address.trim().isEmpty())
            && (suburb == null || suburb.trim().isEmpty())
            && (state == null || state.trim().isEmpty())
            && (postcode == null || postcode.trim().isEmpty());
    }
}

