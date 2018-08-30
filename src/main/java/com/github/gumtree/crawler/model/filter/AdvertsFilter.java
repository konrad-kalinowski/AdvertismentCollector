package com.github.gumtree.crawler.model.filter;

public class AdvertsFilter {
    private final Range<Double> areaRange;

    public AdvertsFilter(Range<Double> areaRange) {
        this.areaRange = areaRange;
    }

    public Range<Double> getAreaRange() {
        return areaRange;
    }
}
