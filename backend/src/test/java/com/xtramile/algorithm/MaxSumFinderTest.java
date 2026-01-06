package com.xtramile.algorithm;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MaxSumFinderTest {

    @Test
    void testFindMaxSum() {
        List<Integer> numbers = Arrays.asList(5, 3, 9, 1, 7, 2);
        int result = MaxSumFinder.findMaxSum(numbers);
        
        assertEquals(16, result); // 9 + 7
    }

    @Test
    void testFindMaxSum_AllSame() {
        List<Integer> numbers = Arrays.asList(5, 5, 5, 5);
        int result = MaxSumFinder.findMaxSum(numbers);
        
        assertEquals(10, result); // 5 + 5
    }

    @Test
    void testFindMaxSum_TwoElements() {
        List<Integer> numbers = Arrays.asList(10, 20);
        int result = MaxSumFinder.findMaxSum(numbers);
        
        assertEquals(30, result);
    }

    @Test
    void testFindMaxSum_NegativeNumbers() {
        List<Integer> numbers = Arrays.asList(-5, -2, -10, -1);
        int result = MaxSumFinder.findMaxSum(numbers);
        
        assertEquals(-3, result); // -2 + -1
    }
}

