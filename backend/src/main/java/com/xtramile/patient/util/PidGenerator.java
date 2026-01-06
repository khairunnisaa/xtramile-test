package com.xtramile.patient.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public final class PidGenerator {

    private PidGenerator() {
    }

    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis() % 1000000);
    private static final String PREFIX = "MRN";

    public static String generate() {
        long sequence = counter.incrementAndGet();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String sequenceStr = String.format("%06d", sequence % 1000000);
        return PREFIX + timestamp + sequenceStr;
    }

    public static String generateMedicareFormat() {
        long base = System.currentTimeMillis() % 10000000000L;
        String baseStr = String.format("%010d", base);
        
        int[] digits = new int[11];
        for (int i = 0; i < 10; i++) {
            digits[i] = Character.getNumericValue(baseStr.charAt(i));
        }
        
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += digits[i] * weights[i];
        }
        
        int remainder = sum % 10;
        int checkDigit = remainder == 0 ? 0 : 10 - remainder;
        digits[10] = checkDigit;
        
        StringBuilder sb = new StringBuilder();
        for (int digit : digits) {
            sb.append(digit);
        }
        
        return PidValidator.format(sb.toString());
    }
}

