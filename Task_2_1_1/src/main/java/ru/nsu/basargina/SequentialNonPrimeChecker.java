package ru.nsu.basargina;

import java.util.List;

/**
 * Class for determining not primes in array using sequential execution.
 */
public class SequentialNonPrimeChecker implements NonPrimeChecker {
    /**
     * Checks if an array has not prime number.
     *
     * @param array - array with numbers
     * @return true if array has not prime number
     */
    @Override
    public boolean hasNonPrime(List<Integer> array) {
        for (int num : array) {
            if (!NonPrimeCheckerUtils.isPrime(num)) {
                return true;
            }
        }
        return false;
    }
}
