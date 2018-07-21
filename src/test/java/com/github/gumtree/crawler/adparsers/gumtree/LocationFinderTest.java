package com.github.gumtree.crawler.adparsers.gumtree;

import com.github.gumtree.crawler.adparsers.LocationFinder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.stream.Stream;

class LocationFinderTest {

    @ParameterizedTest
    @MethodSource("paramsProvider")
    void findLocationInDesc(String description, String street) {
        LocationFinder locationFinder = new LocationFinder(Collections.EMPTY_SET);
        String locationInDesc = locationFinder.findLocationInDesc(description);
        Assertions.assertThat(!locationInDesc.isEmpty());
        Assertions.assertThat(locationInDesc.contains(street));
        Assertions.assertThat(locationInDesc).isEqualTo(street);
    }

    private static Stream<Arguments> paramsProvider() {
        return Stream.of(
                Arguments.of("Mieszkanie w Sosnowcu na ul. Kieleckiej. Mieszkanie wyremontowane 3 pokoje, kuchnie , wc, łazienka. " +
                        "W bloku wymienione piony. Spokojna okolica. W mieszkaniu 3 szafy zabudowane, kuchnia w zabudowie. Kontakt Łukasz 5" +
                        "4. W cenę najmu wliczony szybki Internet oraz ogrzewanie bez limitu. Mieszkanie bez właściciela", "Kieleckiej"),
                Arguments.of("", "")
        );
    }
}