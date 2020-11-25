package com.client.domain;

public class Pair {
    private final int first;
    private final int last;

    Pair(int first, int last) {
        this.first = first;
        this.last = last;
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }
}