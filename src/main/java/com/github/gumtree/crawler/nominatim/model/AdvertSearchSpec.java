package com.github.gumtree.crawler.nominatim.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdvertSearchSpec {

    private final String city;
    private final String country;
    private final List<String> links;

    public AdvertSearchSpec(String city, String country, String... links) {
        this.city = city;
        this.country = country;
        this.links = Arrays.asList(links);
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public List<String> getLinks() {
        return links;
    }
}
