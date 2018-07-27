package com.github.gumtree.crawler.adparsers;


import com.github.gumtree.crawler.model.StreetType;
import com.github.gumtree.crawler.util.InflectionsFinder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LocationFinder {
    private static final Logger log = LoggerFactory.getLogger(LocationFinder.class);
    private static final Logger locations = LoggerFactory.getLogger("locations");

    private static final int MAX_STREET_NAME_LENGTH = 4;
    public static final String SENTENCE_SPLIT_REGEX = "(?<!\\w\\.\\w.)" +
            "(?<!^\\w\\.\\w.)" +
            "(?<!\\s[a-zA-Z]{2}\\.)" +
            "(?<=\\.|\\?)\\s";
    private static final Set<String> STREET_PREFIXES = StreetType.allStreetNamePrefixes();

    private final InflectionsFinder inflectionsFinder;

    @Autowired
    public LocationFinder(InflectionsFinder inflectionsFinder) {
        this.inflectionsFinder = inflectionsFinder;
    }

    public Set<String> findLocationInDesc(Map<StreetType, Set<String>> availableStreets, String description) {
        Set<String> locationFound = new HashSet<>();
        List<String> sentences = Arrays.asList(description.split(SENTENCE_SPLIT_REGEX));
        for (String sentence : sentences) {
            List<String> words = Arrays.stream(sentence
                    .replaceAll("[.,()]", "")
                    .split("[ /]"))
                    .filter(word -> StringUtils.isNotBlank(word))
                    .collect(Collectors.toList());

            for (int i = 0; i < words.size(); i++) {
                if (STREET_PREFIXES.contains(words.get(i).toLowerCase())) {
                    List<String> streetContainingPart = words.subList(i, words.size());
                    if (streetContainingPart.size() > 1) {
                        String streetName = getStreetName(availableStreets, streetContainingPart);
                        Optional<Integer> buldingNumber = findBuldingNumber(streetContainingPart, streetName);
                        if (!streetName.isEmpty()) {
                            if (buldingNumber.isPresent()) {
                                streetName = streetName + " " + buldingNumber.get();
                            }
                            locationFound.add(streetName);
                            locations.info("{}, {}", streetName, sentence);
                        } else {
                            locations.warn("{}", sentence);
                        }
                    }
                }
            }
        }

        return locationFound;
    }

    private String findStreet(Map<StreetType, Set<String>> availableStreets, StreetType streetType, List<String> inflections) {
        for (String inflection : inflections) {
            if (availableStreets.get(streetType).contains(inflection)) {
                return inflection;
            }
        }
        log.info("Couldn't find street name inflections {}", inflections);
        return "";
    }

    private String getStreetName(Map<StreetType, Set<String>> availableStreets, List<String> streetContainingPart) {
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
        return findStreet(availableStreets, streetType, inflections);
    }

    private Optional<Integer> findBuldingNumber(List<String> streetContainingPart, String streetName) {
        int streetNameLength = streetName.split(" ").length;
        if (streetContainingPart.size() > streetNameLength + 1) {
            String maybeBuildingNumber = streetContainingPart.get(streetNameLength + 1);
            if (StringUtils.isNumeric(maybeBuildingNumber)) {
                return Optional.of(Integer.parseInt(maybeBuildingNumber));
            }
        }
        return Optional.empty();

    }

}

