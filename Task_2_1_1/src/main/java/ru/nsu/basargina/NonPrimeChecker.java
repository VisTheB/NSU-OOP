package ru.nsu.basargina;

import java.util.List;

/**
 * The interface that all solutions should implement is sequential, through threads, and parallel.
 */
public interface NonPrimeChecker {
    /**
     * @param array array with numbers
     * @return true if the array has at least one not prime number
     */
    boolean hasNonPrime(List<Integer> array);
}
