package com.github.gumtree.crawler.model.filter;

public class AdvertsFilter {
    private final Range<Double> areaRange;
    private final Range<Double> priceRange;
    private final String searchQuery;

    public AdvertsFilter(Range<Double> areaRange, Range<Double> priceRange, String searchQuery) {
        this.areaRange = areaRange;
        this.priceRange = priceRange;
        this.searchQuery = searchQuery;
    }

    public Range<Double> getAreaRange() {
        return areaRange;
    }

    public Range<Double> getPriceRange() {
        return priceRange;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
