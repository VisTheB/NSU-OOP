package ru.nsu.basargina;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for determining not primes in array using threads.
 */
public class ThreadedNonPrimeChecker implements NonPrimeChecker {
    private final int threadsNumber;

    /**
     * Constructor for the thread.
     *
     * @param threadsNumber - number of threads to be created
     */
    public ThreadedNonPrimeChecker(int threadsNumber) {
        this.threadsNumber = threadsNumber;
    }

    /**
     * Checks if an array has not prime number.
     *
     * @param array - array with numbers
     * @return true if array has not prime number
     */
    @Override
    public boolean hasNonPrime(List<Integer> array) {
        final int arrLen = array.size();
        
        if (arrLen == 0) {
            return false;
        }

        AtomicBoolean foundNonPrime = new AtomicBoolean(false);

        // Divide the array into segments
        int segmentSize = (int) Math.ceil((double) arrLen / threadsNumber);

        final Thread[] threads = new Thread[threadsNumber];

        for (int i = 0; i < threadsNumber; i++) {
            final int start = i * segmentSize;
            final int end = Math.min((i + 1) * segmentSize, arrLen);

            threads[i] = new Thread(() -> {
                for (int j = start; j < end && !foundNonPrime.get(); j++) {
                    if (!NonPrimeCheckerUtils.isPrime(array.get(j))) {
                        foundNonPrime.set(true);
                        break;
                    }
                }
            });
            int finalI = i;
            threads[i].setUncaughtExceptionHandler(
                (thread, exception) -> {
                    synchronized (System.err) {
                        System.err.println("Thread " + finalI + " :");
                        exception.printStackTrace(System.err);
                    }
                });
            threads[i].start();
        }

        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Something went wrong", e);
        }

        return foundNonPrime.get();
    }
}
