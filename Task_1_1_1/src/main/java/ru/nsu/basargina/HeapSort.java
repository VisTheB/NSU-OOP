package ru.nsu.basargina;

/**
 * The HeapSort class with heapsort() and heapify() functions.
 */

public class HeapSort {

    /**
     * Function that returns sorted array.
     *
     * @param arr - input array
     */
    public static void heapsort(int[] arr) {

        int len = arr.length;

        for (int i = len / 2 - 1; i >= 0; i--) { // creating initial heap
            heapify(arr, len, i);
        }

        for (int i = len - 1; i >= 0; i--) { // swap the greatest elem to the end
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        }
    }

    /**
     * Function that makes binary heap from subtree with root i.
     *
     * @param arr - input array
     * @param n - size of the heap
     * @param i - root
     */
    private static void heapify(int[] arr, int n, int i) {

        int l = 2*i + 1; // left node
        int r = 2*i +2; // right node
        int max = i;

        if (l < n && arr[l] > arr[max]) {
            max = l;
        }
        if (r < n && arr[r] > arr[max]) {
            max = r;
        }

        if (max != i) { // swap root and node
            int temp = arr[i];
            arr[i] = arr[max];
            arr[max] = temp;

            heapify(arr, n, max);
        }
    }
}