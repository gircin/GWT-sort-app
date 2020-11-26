package com.client.domain;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Данный класс инкапсулирует процесс сортировки
 * переданного массива методом QuickSort и
 * сбора данных о последовательности перемещения элементов этого массива.
 * <p>
 * Данные об индексе элементов, которые перемещаются,
 * хранятся в объекте класса Pair.
 * Создаваемые объекты Pair хранятся в списке ArrayList и доступны для последующей итерации
 * <p>
 * Оригинальный массив из класса SortView не перезаписывается, пока не отсортируются все элементы
 * Вместо этого, объекту класса Sort передаётся оригинальный массив и с него делается копия во
 * внутренний массив, используя быстрый метод System.arraycopy().
 * <p>
 * Сразу после инициализации объекта, переданный в него массив копируется и копия сортируется
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
     * Простая реализация алгоритма QuickSort, использующая рекурсию.
     * Исходный код скопирован с репозитория
     * https://github.com/eugenp/tutorials/blob/e0a581b6e1b482db3593acf7331d3468f553259d/algorithms-sorting/src/main/java/com/baeldung/algorithms/quicksort/ThreeWayQuickSort.java
     * Эфективность примерно в 3 раза ниже, чем у кода DualPivotQuicksort.sort()
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
     * Метод возвращает список объектов Pair,
     * которые представляют из себя историю изменения массива
     */
    public List<Pair> getPairs() {
        return pairs;
    }
}