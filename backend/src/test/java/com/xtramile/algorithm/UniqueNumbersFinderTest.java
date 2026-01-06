package com.xtramile.algorithm;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UniqueNumbersFinderTest {

    @Test
    void testFindUniqueNumbers() {
        List<Integer> input = Arrays.asList(1, 2, 3, 2, 4, 3, 5);
        Set<Integer> result = UniqueNumbersFinder.findUniqueNumbers(input);
        
        Set<Integer> expected = new HashSet<>(Arrays.asList(1, 4, 5));
        assertEquals(expected, result);
    }

    @Test
    void testFindUniqueNumbers_AllUnique() {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5);
        Set<Integer> result = UniqueNumbersFinder.findUniqueNumbers(input);
        
        Set<Integer> expected = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(expected, result);
    }

    @Test
    void testFindUniqueNumbers_AllDuplicates() {
        List<Integer> input = Arrays.asList(1, 1, 2, 2, 3, 3);
        Set<Integer> result = UniqueNumbersFinder.findUniqueNumbers(input);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindUniqueNumbers_EmptyList() {
        List<Integer> input = Collections.emptyList();
        Set<Integer> result = UniqueNumbersFinder.findUniqueNumbers(input);
        
        assertTrue(result.isEmpty());
    }
}

