package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.nsu.basargina.HeapSort.heapsort;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

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

        int[] arr = new int[100000]; // generate array and fill it with random numbers
        Arrays.fill(arr, random.nextInt());

        long start = System.nanoTime();
        heapsort(arr);
        long finish = System.nanoTime();

        long timeTaken = finish - start;

        assertTrue(timeTaken < 1000000000);
    }
}