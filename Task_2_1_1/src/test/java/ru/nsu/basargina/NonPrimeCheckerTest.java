package ru.nsu.basargina;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

public class NonPrimeCheckerTest {
    @ParameterizedTest()
    @ArgumentsSource(NonPrimeCheckerProvider.class)
    public void testWithoutPrimeNumbers(NonPrimeChecker nonPrimeChecker) {
        List<Integer> bigPrimeArray = generateBigPrimeArray(100, false);
        Assertions.assertFalse(nonPrimeChecker.hasNonPrime(bigPrimeArray));
    }

    @ParameterizedTest()
    @ArgumentsSource(NonPrimeCheckerProvider.class)
    public void testWithPrimeNumbers(NonPrimeChecker nonPrimeChecker) {
        List<Integer> bigPrimeArray = generateBigPrimeArray(100, true);
        Assertions.assertTrue(nonPrimeChecker.hasNonPrime(bigPrimeArray));
    }

    @ParameterizedTest()
    @ArgumentsSource(NonPrimeCheckerProvider.class)
    public void testEmpty(NonPrimeChecker nonPrimeChecker) {
        List<Integer> bigPrimeArray = new ArrayList<>();
        Assertions.assertFalse(nonPrimeChecker.hasNonPrime(bigPrimeArray));
    }

    private List<Integer> generateBigPrimeArray(int size, boolean addNotPrime) {
        List<Integer> samplePrimes = new ArrayList<>();

        Collections.addAll(samplePrimes, 999983, 1000003, 1000033, 1000037, 1000039,
                1000081, 1000099, 1000117, 1000121, 1000133);
        int len = samplePrimes.size();

        List<Integer> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(samplePrimes.get(i % len));
        }

        if (addNotPrime) {
            result.add(4);
        }

        return result;
    }

    private static class NonPrimeCheckerProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new SequentialNonPrimeChecker()),
                    Arguments.of(new ThreadedNonPrimeChecker(1)),
                    Arguments.of(new ThreadedNonPrimeChecker(3)),
                    Arguments.of(new ThreadedNonPrimeChecker(5)),
                    Arguments.of(new ParallelNonPrimeChecker()));
        }
    }
}

