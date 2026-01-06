package com.xtramile.patient.util;

import java.util.regex.Pattern;

public final class PidValidator {

    private PidValidator() {
    }

    private static final Pattern MEDICARE_PATTERN = Pattern.compile("^\\d{10}\\d{1}$");
    private static final Pattern IHI_PATTERN = Pattern.compile("^\\d{16}$");
    private static final Pattern MRN_PATTERN = Pattern.compile("^[A-Z0-9]{6,12}$");

    public static boolean isValid(String pid) {
        if (pid == null || pid.isEmpty()) {
            return false;
        }

        String normalized = normalize(pid);
        
        return isValidMedicare(normalized) 
            || isValidIHI(normalized) 
            || isValidMRN(normalized);
    }

    public static boolean isValidMedicare(String pid) {
        if (pid == null) return false;
        String normalized = normalize(pid);
        if (normalized.length() != 11) return false;
        
        if (!MEDICARE_PATTERN.matcher(normalized).matches()) {
            return false;
        }
        
        return validateMedicareChecksum(normalized);
    }

    public static boolean isValidIHI(String pid) {
        if (pid == null) return false;
        String normalized = normalize(pid);
        return normalized.length() == 16 && IHI_PATTERN.matcher(normalized).matches();
    }

    public static boolean isValidMRN(String pid) {
        if (pid == null) return false;
        String normalized = normalize(pid);
        return MRN_PATTERN.matcher(normalized).matches();
    }

    public static String format(String pid) {
        if (pid == null || pid.isEmpty()) {
            return null;
        }

        String normalized = normalize(pid);
        
        if (normalized.length() == 11 && isValidMedicare(normalized)) {
            return formatMedicare(normalized);
        }
        
        if (normalized.length() == 16 && isValidIHI(normalized)) {
            return formatIHI(normalized);
        }
        
        return normalized;
    }

    private static String normalize(String pid) {
        return pid.replaceAll("[^A-Z0-9]", "").toUpperCase();
    }

    private static String formatMedicare(String pid) {
        if (pid.length() != 11) return pid;
        return pid.substring(0, 4) + " " + pid.substring(4, 9) + " " + pid.substring(9, 11);
    }

    private static String formatIHI(String pid) {
        if (pid.length() != 16) return pid;
        return pid.substring(0, 4) + " " + pid.substring(4, 8) + " " + pid.substring(8, 12) + " " + pid.substring(12, 16);
    }

    private static boolean validateMedicareChecksum(String pid) {
        if (pid.length() != 11) return false;
        
        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = Character.getNumericValue(pid.charAt(i));
        }
        
        int sum = 0;
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        
        for (int i = 0; i < 10; i++) {
            sum += digits[i] * weights[i];
        }
        
        int remainder = sum % 10;
        int checkDigit = remainder == 0 ? 0 : 10 - remainder;
        
        return checkDigit == digits[10];
    }

    public static String getFormatDescription() {
        return "PID format: Medicare (11 digits), IHI (16 digits), or MRN (6-12 alphanumeric)";
    }
}

