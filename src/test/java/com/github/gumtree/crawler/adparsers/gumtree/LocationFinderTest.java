package com.github.gumtree.crawler.adparsers.gumtree;

import com.github.gumtree.crawler.adparsers.LocationFinder;
import com.github.gumtree.crawler.util.InflectionsFinder;
import com.google.common.collect.Sets;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class LocationFinderTest {

    @ParameterizedTest
    @CsvFileSource(resources = {"/locationFinderTest.csv"})
    void findLocationInDesc(String description, @ConvertWith(ToSetConverter.class) Set<String> street) {

        HashSet<String> availableStreets = Sets.newHashSet("borsucza", "orawska", "bronowicka",
                "niemcewicza", "piastów", "kobierzyńska", "obozowa", "starowiślna");
        LocationFinder locationFinder = new LocationFinder(availableStreets, new InflectionsFinder());
        Set<String> locationInDesc = locationFinder.findLocationInDesc(description);
        Assertions.assertThat(locationInDesc).isNotEmpty();
        Assertions.assertThat(locationInDesc).containsExactlyInAnyOrder(street.toArray(new String[]{}));

    }

    private static final class ToSetConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
            String[] words = ((String) o).split(",");
            return Sets.newHashSet(words);
        }
    }

}