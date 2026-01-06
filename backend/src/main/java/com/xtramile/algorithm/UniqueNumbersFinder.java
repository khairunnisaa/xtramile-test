package com.xtramile.algorithm;

import java.util.*;

public class UniqueNumbersFinder {

    public static Set<Integer> findUniqueNumbers(List<Integer> input) {
        Map<Integer, Integer> freq = new HashMap<>();

        for (Integer n : input) {
            freq.put(n, freq.getOrDefault(n, 0) + 1);
        }

        Set<Integer> result = new HashSet<>();
        for (Map.Entry<Integer, Integer> e : freq.entrySet()) {
            if (e.getValue() == 1) {
                result.add(e.getKey());
            }
        }
        return result;
    }
}

