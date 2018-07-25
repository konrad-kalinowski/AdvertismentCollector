package com.github.gumtree.crawler.adparsers;


import com.github.gumtree.crawler.model.StreetType;
import com.github.gumtree.crawler.util.InflectionsFinder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LocationFinder {
    private static final Logger log = LoggerFactory.getLogger(LocationFinder.class);
    private static final Logger locations = LoggerFactory.getLogger("locations");

    private static final int MAX_STREET_NAME_LENGTH = 4;
    public static final String SENTENCE_SPLIT_REGEX = "(?<!\\w\\.\\w.)" +
            "(?<!^\\w\\.\\w.)" +
            "(?<!\\s[a-zA-Z]{2}\\.)" +
            "(?<=\\.|\\?)\\s";
    private static final Set<String> STREET_PREFIXES = StreetType.allStreetNamePrefixes();

    private final Map<StreetType, Set<String>> availableStreets;
    private final InflectionsFinder inflectionsFinder;

    public LocationFinder(Map<StreetType, Set<String>> streetTypesToNames, InflectionsFinder inflectionsFinder) {
        this.availableStreets = streetTypesToNames;
        this.inflectionsFinder = inflectionsFinder;
    }

    public Set<String> findLocationInDesc(String description) {
        Set<String> locationFound = new HashSet<>();
        List<String> sentences = Arrays.asList(description.split(SENTENCE_SPLIT_REGEX));
        for (String sentence : sentences) {
            List<String> words = Arrays.stream(sentence
                    .replaceAll("[.,()/]", "")
                    .split(" "))
                    .filter(word -> StringUtils.isNotBlank(word))
                    .collect(Collectors.toList());
            for (int i = 0; i < words.size(); i++) {
                if (STREET_PREFIXES.contains(words.get(i).toLowerCase())) {
                    String streetName = getStreetName(words.subList(i, words.size()));
                    if (!streetName.isEmpty()) {
                        locationFound.add(streetName);
                    }
                }
            }
        }

        return locationFound;
    }

    private String findStreet(StreetType streetType, List<String> inflections) {
        for (String inflection : inflections) {
            if (availableStreets.get(streetType).contains(inflection)) {
                return inflection;
            }
        }
        log.info("Couldn't find street name inflections {}", inflections);
        return "";
    }

    private String getStreetName(List<String> streetContainingPart) {
        StreetType streetType = StreetType.findStreetType(streetContainingPart.get(0));
        for (int i = MAX_STREET_NAME_LENGTH; i >= 1; i--) {
            List<String> possibleStreetName = streetContainingPart.subList(1,
                    streetContainingPart.size() > i ? i + 1 : streetContainingPart.size());
            String streetNameString = String.join(" ", possibleStreetName).toLowerCase();
            log.debug("Checking if {} {} exists", streetType.getName(), streetNameString);
            if (availableStreets.get(streetType).contains(streetNameString)) {
                log.debug("{} {} exists, returning it", streetType.getName(), streetNameString);
                return streetNameString;
            }
        }

        String location = streetContainingPart.get(1);
        log.debug("Checking if 1-part street name exists {}", location);
        List<String> inflections = inflectionsFinder.findInflections(location);
        return findStreet(streetType, inflections);
    }

}

