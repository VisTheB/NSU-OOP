package ru.nsu.basargina;

import java.util.StringTokenizer;

/**
 * Util class for detecting composite numbers in the string.
 */
public class PrimeDetector {
    private final String task;

    /**
     * Create prime detector with given string with numbers.
     *
     * @param task string in the format "TASK taskId n1 n2 … nk"
     */
    public PrimeDetector(String task) {
        this.task = task;
    }

    /**
     * Process string "TASK taskId n1 n2 … nk".
     *
     * @return if composite number has been found, break and return string with result and taskId
     */
    public String findComposite() {
        StringTokenizer st = new StringTokenizer(task);
        st.nextToken(); // "TASK"
        String taskId = st.nextToken();
        System.out.println("pd 1");
        boolean allPrime = true;

        while (st.hasMoreTokens()) {
            int n = Integer.parseInt(st.nextToken());
            System.out.println("pd n " + n);
            if (!isPrime(n)) {
                allPrime = false;
                break;
            }
        }
        System.out.println("pd 2");
        if (allPrime) {
            return "RESULT " + taskId + " true";
        } else {
            return "RESULT " + taskId +  " false";
        }
    }

    /**
     * Util method for checking prime numbers.
     *
     * @param n number to be checked
     * @return true if number is prime
     */
    private static boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }
        for (int d = 3; d * d <= n; d += 2) {
            if (n % d == 0) {
                return false;
            }
        }
        return true;
    }
}
