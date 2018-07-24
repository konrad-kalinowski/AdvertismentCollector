package com.github.gumtree.crawler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class InflectionsFinder {
    private static final Logger log = LoggerFactory.getLogger(InflectionsFinder.class);

    public List<String> findInflections(String phrase) {
        URL dictionaryUrl = InflectionsFinder.class.getResource("/polish_inflection_dictionary.txt");

        String searchPhrase = phrase.toLowerCase();
        try (Stream<String> stream = Files.lines(Paths.get(dictionaryUrl.toURI()))) {
            return stream
                    .map(line -> Arrays.asList(line.toLowerCase().split(", ")))
                    .filter(line -> line.contains(searchPhrase))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("No inflections found for phrase " + searchPhrase));
        } catch (IOException | URISyntaxException e) {
            log.error("Failed to open file", e);
        }
        return Collections.singletonList(phrase);
    }
}
