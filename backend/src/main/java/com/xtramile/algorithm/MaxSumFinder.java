package com.xtramile.algorithm;

import java.util.List;

public class MaxSumFinder {

    public static int findMaxSum(List<Integer> numbers) {
        if (numbers == null || numbers.size() < 2) {
            return 0;
        }
        
        int max1 = Integer.MIN_VALUE;
        int max2 = Integer.MIN_VALUE;

        for (int n : numbers) {
            if (n > max1) {
                max2 = max1;
                max1 = n;
            } else if (n > max2) {
                max2 = n;
            }
        }
        return max1 + max2;
    }
}

