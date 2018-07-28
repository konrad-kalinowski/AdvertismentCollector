package com.github.gumtree.crawler.model;

import java.util.List;

public class AdLinks {

    private final String country;
    private final String city;
    private final List<String> links;

    public AdLinks(String country, String city, List<String> links) {
        this.country = country;
        this.city = city;
        this.links = links;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public List<String> getLinks() {
        return links;
    }
}
