package ru.nsu.basargina;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Class for determining not primes in array using parallel stream.
 */
public class ParallelNonPrimeChecker implements NonPrimeChecker {
    /**
     * Checks if an array has not prime number.
     *
     * @param array - array with numbers
     * @return true if array has not prime number
     */
    @Override
    public boolean hasNonPrime(List<Integer> array) {
        return array.stream()
                .parallel()
                .anyMatch(num -> !NonPrimeCheckerUtils.isPrime(num));
    }
}
