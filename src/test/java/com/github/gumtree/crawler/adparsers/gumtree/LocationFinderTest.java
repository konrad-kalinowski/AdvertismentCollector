package com.github.gumtree.crawler.adparsers.gumtree;

import com.github.gumtree.crawler.adparsers.LocationFinder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LocationFinderTest {

    @Test
    void findLocationInDesc() {
        LocationFinder locationFinder = new LocationFinder();
        String locationInDesc = locationFinder.findLocationInDesc("Mieszkanie w Sosnowcu na ul. Kieleckiej. Mies" +
                "zkanie wyremontowane 3 pokoje, kuchnie , wc, łazienka. W bloku wymienione piony. Spokojna okolica. W mieszkaniu 3 szafy zabudowane, kuchnia w zabudowie. Kontakt Łukasz 5" +
                "4. W cenę najmu wliczony szybki Internet oraz ogrzewanie bez limitu. Mieszkanie bez właściciela");
        Assertions.assertThat(!locationInDesc.isEmpty());
        Assertions.assertThat(locationInDesc.contains("Kieleckiej."));
        Assertions.assertThat(locationInDesc).isEqualTo("Kieleckiej.");

    }
}