package com.github.gumtree.crawler.adparsers.gumtree;

import com.github.gumtree.crawler.adparsers.LocationFinder;
import com.github.gumtree.crawler.util.InflectionsFinder;
import com.google.common.collect.Sets;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Collections;
import java.util.HashSet;

class LocationFinderTest {

    @ParameterizedTest
    @CsvFileSource(resources = {"/locationFinderTest.csv"})
    void findLocationInDesc(String description, String street) {

        HashSet<String> availableStreets = Sets.newHashSet("borsucza", "orawska", "bronowicka",
                "niemcewicza", "piastów", "kobierzyńska", "starowiślna");
        LocationFinder locationFinder = new LocationFinder(availableStreets, new InflectionsFinder());
        String locationInDesc = locationFinder.findLocationInDesc(description);
        Assertions.assertThat(!locationInDesc.isEmpty());
        Assertions.assertThat(locationInDesc).isEqualTo(street);
    }

}