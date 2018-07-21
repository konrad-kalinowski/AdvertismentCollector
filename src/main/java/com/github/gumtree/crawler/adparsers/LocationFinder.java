package com.github.gumtree.crawler.adparsers;


import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class LocationFinder {
    private static final Logger locations = LoggerFactory.getLogger("locations");

    public static final String SENTENCE_SPLIT_REGEX = "(?<!\\w\\.\\w.)(?<!\\s[a-zA-Z]{2}\\.)(?<=\\.|\\?)\\s";
    private static final Set<String> STREET_PREFIXES = Sets.newHashSet("ul.", "ul", "al.", "al");

    private final Set<String> availableStreets;

    public LocationFinder(Set<String> availableStreets) {
        this.availableStreets = availableStreets;
    }

    public String findLocationInDesc(String description) {

        String location = "";
        List<String> sentences = Arrays.asList(description.split(SENTENCE_SPLIT_REGEX));
        for (String sentence : sentences) {
            List<String> words = Arrays.asList(sentence.replace(".", "").split(" "));
            for (int i = 0; i < words.size(); i++) {
                if (STREET_PREFIXES.contains(words.get(i))) {
                    location = words.get(i + 1);
                    locations.info("{},{}", location, sentence);
                }
            }
        }

        return location;
    }

}

