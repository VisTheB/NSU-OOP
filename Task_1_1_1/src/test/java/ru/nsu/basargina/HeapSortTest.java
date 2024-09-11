package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.nsu.basargina.HeapSort.heapsort;

import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

/** Class with tests. */
public class HeapSortTest {
    @Test
    void test_regular() {
        int[] arr = {100, 1, 13, 46, 62, 7};
        int[] expected = {1, 7, 13, 46, 62, 100};
        heapsort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void test_similar() {
        int[] arr = {7, 7, 7, 7, 7, 7};
        int[] expected = {7, 7, 7, 7, 7, 7};
        heapsort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void test_sorted() {
        int[] arr = {10, 11, 12, 13, 14, 15, 16};
        int[] expected = {10, 11, 12, 13, 14, 15, 16};
        heapsort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void test_reversed_sorted() {
        int[] arr = {16, 15, 14, 13, 12, 11, 10};
        int[] expected = {10, 11, 12, 13, 14, 15, 16};
        heapsort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void test_single() {
        int[] arr = {55};
        int[] expected = {55};
        heapsort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void test_empty() {
        int[] arr = {};
        int[] expected = {};
        heapsort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void test_negative() {
        int[] arr = {-6, -3, -9, -2, -4};
        int[] expected = {-9, -6, -4, -3, -2};
        heapsort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void test_time_complexity() {
        Random random = new Random();
        double constNlogN = 0; // constant

        int initLen = 1000000;
        for (int len = initLen; len <= initLen * 9; len *= 3) {
            int[] arr = new int[len]; // generate array and fill it with random numbers
            Arrays.fill(arr, random.nextInt());

            long start = System.nanoTime();
            heapsort(arr);
            long finish = System.nanoTime();
            long timeTaken = (finish - start) / 1000000; // time in miliseconds

            double opNlogN = len * Math.log(len); // quantity of operations

            if (len == 1000000) {
                constNlogN = opNlogN / timeTaken;
            }

            double timeExpected = opNlogN / constNlogN;

            assertTrue(timeTaken <= timeExpected);
            System.out.printf(
                    "Array size: %d, actual time: %d, expected time: %f",
                    len, timeTaken, timeExpected);
        }
    }
}