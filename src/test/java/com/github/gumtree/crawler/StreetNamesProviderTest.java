package com.github.gumtree.crawler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class StreetNamesProviderTest {

    @Test
    void findCitySymbolInSincDB() {
        StreetNamesProvider streetNamesProvider = new StreetNamesProvider();
        int citySymbolInSincDB = streetNamesProvider.findCitySymbolInSincDB(12, 61, 05, "Kraków");
        Assertions.assertThat(citySymbolInSincDB).isEqualTo(951327);

        Set<String> streets = streetNamesProvider.findStreets(citySymbolInSincDB);
        Assertions.assertThat(streets).isNotEmpty();
        Assertions.assertThat(streets.size()).isEqualTo(492);
        Assertions.assertThat(streets).contains("czyżyńska");
        Assertions.assertThat(streets).contains("ludwika zieleniewskiego");
    }


}