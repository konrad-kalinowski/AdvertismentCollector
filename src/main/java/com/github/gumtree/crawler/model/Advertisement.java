package com.github.gumtree.crawler.model;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.Set;

public class Advertisement {
    public static final double VALUE_NOT_SET = Double.MIN_VALUE;

    private final Integer id;
    private final String title;
    private final String link;
    private final double price;
    private final String description;
    private final String city;
    private Set<String> streets;
    private final String country;
    private final double area;
    private double pricePerSquareMeter;
    private Coordinates coordinates;


    private Advertisement(String title, String link,
                          double price, String description,
                          Set<String> streets, String country,
                          String city, double area,
                          double pricePerSquareMeter, Coordinates coordinates) {
        this(null,title,link,price,description,city,streets,country, area, pricePerSquareMeter,coordinates);
    }

    public Advertisement(Integer id, String title,
                         String link, double price,
                         String description, String city,
                         Set<String> streets, String country,
                         double area, double pricePerSquareMeter,
                         Coordinates coordinates) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.price = price;
        this.description = description;
        this.city = city;
        this.streets = streets;
        this.country = country;
        this.area = area;
        this.pricePerSquareMeter = pricePerSquareMeter;
        this.coordinates = coordinates;
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

    public String getCity() {
        return city;
    }

    public double getArea() {
        return area;
    }

    public double getPricePerSquareMeter() {
        return pricePerSquareMeter;
    }

    public String getCountry() {
        return country;
    }

    public void setPossibleAddresses(Set<String> locationInDesc) {
        this.streets = locationInDesc;
    }

    public Set<String> getStreets() {
        return streets;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Integer getId() {
        return id;
    }

    public void setCoodrinates(Coordinates coodrinates) {
        this.coordinates = coodrinates;
    }


    public static class AdvertBuilder {
        private final String title;
        private final String link;
        private double price;
        private String description;
        private Set<String> streets;
        private String city;
        private String country;
        private double area;
        private Double pricePerSquareMeter;
        private Coordinates coordinates;
        private Integer id;

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

        public AdvertBuilder city(String city) {
            this.city = city;
            return this;
        }

        public AdvertBuilder country(String country) {
            this.country = country;
            return this;
        }

        public AdvertBuilder area(double area) {
            this.area = area;
            return this;

        }

        public AdvertBuilder streets(Set<String> streets) {
            this.streets = streets;
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
            return new Advertisement(id, title, link, price, description, city, streets, country, area, priceForSquare, coordinates);
        }

        public AdvertBuilder pricePerSquareMeter(Double pricePerSquareMeter) {
            this.pricePerSquareMeter = pricePerSquareMeter;
            return this;
        }

        public AdvertBuilder coordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public AdvertBuilder id(Integer id) {
            this.id = id;
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
                ", city='" + city + '\'' +
                ", area=" + area +
                '}';
    }
}
