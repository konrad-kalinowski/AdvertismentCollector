package com.github.gumtree.crawler.model;

import java.util.Collections;
import java.util.Set;

public class Advertisement {
    public static final double VALUE_NOT_SET = Double.MIN_VALUE;
    private final String title;
    private final String link;
    private final double price;
    private final String description;
    private final String location;
    private final double area;
    private Set<String> addresses;
    private double priceForSquare;


    private Advertisement(String title, String link, double price, String description, String location, double area, Set<String> addresses, double priceForSquare) {
        this.title = title;
        this.link = link;
        this.price = price;
        this.description = description;
        this.location = location;
        this.area = area;
        this.addresses = addresses == null ? Collections.EMPTY_SET : this.addresses;
        this.priceForSquare = priceForSquare;
    }

    public static AdvertBuilder builder(String title, String link) {
        return new AdvertBuilder(title, link);
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public double getArea() {
        return area;
    }

    public double getPricePerSquareMeter() {
        return priceForSquare;
    }

    public void setpossibleaddresses(Set<String> locationInDesc) {
        this.addresses = locationInDesc;
    }

    public Set<String> getAddresses() {
        return addresses;
    }

    public static class AdvertBuilder {
        private final String title;
        private final String link;
        private double price;
        private String description;
        private String location;
        private double area;
        private Set<String> addresses;
        private Double
                pricePerSquareMeter;

        public AdvertBuilder(String title, String link) {
            this.title = title;
            this.link = link;
        }

        public AdvertBuilder price(double price) {
            this.price = price;
            return this;
        }

        public AdvertBuilder description(String description) {
            this.description = description;
            return this;
        }

        public AdvertBuilder location(String location) {
            this.location = location;
            return this;
        }

        public AdvertBuilder area(double area) {
            this.area = area;
            return this;

        }

        public AdvertBuilder addresses(Set<String> addresses) {
            this.addresses = addresses;
            return this;
        }

        public Advertisement build() {
            double priceForSquare;
            if (this.pricePerSquareMeter == null) {
                if (area == 0d || area == VALUE_NOT_SET) {
                    priceForSquare = VALUE_NOT_SET;
                } else {
                    priceForSquare = price / area;
                }
            } else {
                priceForSquare = this.pricePerSquareMeter;
            }
            return new Advertisement(title, link, price, description, location, area, addresses, priceForSquare);
        }

        public AdvertBuilder pricePerSquareMeter(Double pricePerSquareMeter) {
            this.pricePerSquareMeter = pricePerSquareMeter;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", area=" + area +
                '}';
    }
}
