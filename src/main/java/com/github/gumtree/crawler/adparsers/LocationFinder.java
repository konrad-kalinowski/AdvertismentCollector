package com.github.gumtree.crawler.adparsers;


import java.util.Arrays;
import java.util.List;

public class LocationFinder {

    public String findLocationInDesc(String description) {
        String location = "";
        List<String> splitDescription = Arrays.asList(description.split(" "));

        for (int i = 0; i < splitDescription.size(); i++) {
            if (splitDescription.get(i).equals("ul") || splitDescription.get(i).equals("ul.")) {
                location = splitDescription.get(i + 1);
            }
        }
        return location;
    }

}

