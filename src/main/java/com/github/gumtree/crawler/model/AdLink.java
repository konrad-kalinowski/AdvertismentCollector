package com.github.gumtree.crawler.model;

public class AdLink {
    private final String link;
    private final String country;
    private final String city;

    public AdLink(String link, String country, String city) {
        this.link = link;
        this.country = country;
        this.city = city;
    }

    public String getLink() {
        return link;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}
