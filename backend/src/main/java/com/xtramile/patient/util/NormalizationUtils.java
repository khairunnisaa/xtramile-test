package com.xtramile.patient.util;

public final class NormalizationUtils {

    private NormalizationUtils() {
    }

    public static String normalize(String value) {
        return value == null ? null : value.trim();
    }

    public static String normalizeForMatching(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    public static String normalizePhone(String phone) {
        return phone == null ? null : phone.replaceAll("\\D", "");
    }

    public static String normalizePhoneForMatching(String phone) {
        return phone == null ? "" : phone.replaceAll("\\D", "");
    }

    public static String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}

