package ru.nsu.basargina;

/**
 * Class with necessary for checking prime numbers function.
 */
public class NonPrimeCheckerUtils {
    /**
     * Check if given number is prime.
     * @param n number
     * @return true, if n is prime
     */
    static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n % 2 == 0) {
            return n == 2;
        }
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
