package com.github.gumtree.crawler.model;

public class Coordinates {

    public static final Coordinates EMPTY_COORDINATES = new Coordinates(null, null);

    private final Double longitude;
    private final Double latitude;

    public Coordinates(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

}
