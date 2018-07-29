package com.github.gumtree.crawler.nominatim.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {
    @JsonProperty("boundingBox")
    private List<String> boundingBox;
    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("lon")
    private double longtitude;
    @JsonProperty("display_name")
    private String displayName;

    public List<String> getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(List<String> boundingBox) {
        this.boundingBox = boundingBox;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "boundingBox=" + boundingBox +
                ", latitude='" + latitude + '\'' +
                ", longtitude='" + longtitude + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
