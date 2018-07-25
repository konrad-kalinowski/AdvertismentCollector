package com.github.gumtree.crawler.adparsers.gumtree;

import com.github.gumtree.crawler.StreetNamesProvider;
import com.github.gumtree.crawler.adparsers.LocationFinder;
import com.github.gumtree.crawler.model.StreetType;
import com.github.gumtree.crawler.util.InflectionsFinder;
import com.google.common.collect.Sets;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

class LocationFinderTest {
    private static final Logger log = LoggerFactory.getLogger(LocationFinderTest.class);

    private static Map<StreetType, Set<String>> AVAILABLE_STREETS;

    @BeforeAll
    public static void setup() {
        StreetNamesProvider streetNamesProvider = new StreetNamesProvider();
        Set<Integer> citySymbolInSincDB = streetNamesProvider.findCitySymbolInSincDB(12, 61, "Kraków");
        AVAILABLE_STREETS = streetNamesProvider.findStreets(citySymbolInSincDB);

        log.debug("Initialized test with {} streets", AVAILABLE_STREETS.size());
        log.debug("Streets: {}", AVAILABLE_STREETS);
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/locationFinderTest.csv"})
    void findLocationInDesc(String description, @ConvertWith(ToSetConverter.class) Set<String> expectedStreets) {

        LocationFinder locationFinder = new LocationFinder(AVAILABLE_STREETS, new InflectionsFinder());
        Set<String> foundLocationInDesc = locationFinder.findLocationInDesc(description);
        Assertions.assertThat(foundLocationInDesc).isNotEmpty();
        Assertions.assertThat(foundLocationInDesc).containsAll(expectedStreets);
    }

    private static final class ToSetConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
            String[] words = ((String) o).split(",");
            return Sets.newHashSet(words);
        }
    }

}