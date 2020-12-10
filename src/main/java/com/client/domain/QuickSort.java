package com.client.domain;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * This class encapsulates the sorting process
 * passed array by QuickSort method and
 * collecting data on the sequence of movement of the elements of this array.
 * <p>
 * Data about the index of the elements that are moved
 * are stored in an object of the Pair class.
 * Created Pair instance are stored in the ArrayList and are available for subsequent iteration
 * <p>
 * The original array from the SortView class is not overwritten until all elements are sorted
 * Instead, the original array is passed to the Sort object and a copy is made from it in
 * internal array using the fast System.arraycopy () method.
 * <p>
 * Immediately after object initialization, the array passed to it is copied and the copy is sorted
 */
public class QuickSort {
    private final int[] arr;
    private final List<Pair> pairs = new LinkedList<>();

    public QuickSort(@NotNull int[] arr) {
        this.arr = new int[arr.length];
        System.arraycopy(arr, 0, this.arr, 0, arr.length);
        sort(0, this.arr.length - 1);
    }

    /**
     * Simple implementation of the QuickSort algorithm using recursion.
     * Source code from
     * https://github.com/eugenp/tutorials/blob/e0a581b6e1b482db3593acf7331d3468f553259d/algorithms-sorting/src/main/java/com/baeldung/algorithms/quicksort/ThreeWayQuickSort.java
     * Efficiency is about 3 times lower than that of the DualPivotQuicksort.sort () code
     */
    private void sort(int begin, int end) {
        if (end <= begin) return;
        int i = begin;
        int less = begin;
        int greater = end;
        while (i <= greater) {
            if (arr[i] > arr[less]) {
                int tmp = arr[i];
                arr[i] = arr[less];
                arr[less] = tmp;
                pairs.add(new Pair(i, less));
                i++;
                less++;
            } else if (arr[less] > arr[i]) {
                int tmp = arr[i];
                arr[i] = arr[greater];
                arr[greater] = tmp;
                pairs.add(new Pair(i, greater));
                greater--;
            } else {
                i++;
            }
        }
        sort(begin, less - 1);
        sort(greater + 1, end);
    }

    /**
     * The method returns a list of Pair objects,
     * which are the history of array changes
     */
    public List<Pair> getPairs() {
        return pairs;
    }
}
