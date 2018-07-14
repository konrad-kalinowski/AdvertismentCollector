package com.github.gumtree.crawler.model;

public class Advertisement {
    private final String title;
    private final String link;
    private final double price;
    private final String description;
    private final String location;
    private final double area;


    private Advertisement(String title, String link, double price, String description, String location, double area) {
        this.title = title;
        this.link = link;
        this.price = price;
        this.description = description;
        this.location = location;
        this.area = area;
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

    public static class AdvertBuilder {
        private final String title;
        private final String link;
        private double price;
        private String description;
        private String location;
        private double area;

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

        public Advertisement build() {
            return new Advertisement(title, link, price, description, location, area);
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
