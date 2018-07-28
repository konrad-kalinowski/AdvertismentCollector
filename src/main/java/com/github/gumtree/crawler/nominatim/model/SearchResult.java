package com.github.gumtree.crawler.nominatim.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {
    @JsonProperty("boundingBox")
    private List<String> boundingBox;
    @JsonProperty("lat")
    private String latitude;
    @JsonProperty("lontitude")
    private double lontitude;
    @JsonProperty("display_name")
    private String displayName;

    public List<String> getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(List<String> boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public double getLontitude() {
        return lontitude;
    }

    public void setLontitude(double lontitude) {
        this.lontitude = lontitude;
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
                ", lontitude='" + lontitude + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
