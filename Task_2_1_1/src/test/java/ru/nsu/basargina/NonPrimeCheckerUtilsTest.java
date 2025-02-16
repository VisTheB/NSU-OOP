package ru.nsu.basargina;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Class with tests for util function.
 */
class NonPrimeCheckerUtilsTest {
    @Test
    public void testZero() {
        int num = 0;
        Assertions.assertFalse(NonPrimeCheckerUtils.isPrime(num));
    }

    @Test
    public void testOne() {
        int num = 1;
        Assertions.assertFalse(NonPrimeCheckerUtils.isPrime(num));
    }

    @Test
    public void testTwo() {
        int num = 2;
        Assertions.assertTrue(NonPrimeCheckerUtils.isPrime(num));
    }

    @Test
    public void testPrime() {
        int num = 13;
        Assertions.assertTrue(NonPrimeCheckerUtils.isPrime(num));
    }

    @Test
    public void testNotPrime() {
        int num = 27;
        Assertions.assertFalse(NonPrimeCheckerUtils.isPrime(num));
    }
}