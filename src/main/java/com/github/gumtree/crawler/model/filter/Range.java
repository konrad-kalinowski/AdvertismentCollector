package com.github.gumtree.crawler.model.filter;

public class Range<T> {
    private final T min;
    private final T max;

    public Range(T min, T max) {
        this.min = min;
        this.max = max;
    }

    public T getMax() {
        return max;
    }

    public T getMin() {
        return min;
    }
}
