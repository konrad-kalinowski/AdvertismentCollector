package com.github.gumtree.crawler;

import com.github.gumtree.crawler.model.StreetType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

class StreetNamesProviderTest {

    @Test
    void findCitySymbolInSincDB() {
        StreetNamesProvider streetNamesProvider = new StreetNamesProvider();
        Set<Integer> citySymbolInSincDB = streetNamesProvider.findCitySymbolInSincDB(12, 61,  "Kraków");
        Assertions.assertThat(citySymbolInSincDB).containsExactlyInAnyOrder(950718, 951327, 950470, 950960);

        Map<StreetType, Set<String>> streets = streetNamesProvider.findStreets(citySymbolInSincDB);
        Assertions.assertThat(streets).isNotEmpty();
        Assertions.assertThat(streets.size()).isEqualTo(7);
        Assertions.assertThat(streets.get(StreetType.STREET)).contains("czyżyńska");
        Assertions.assertThat(streets.get(StreetType.STREET)).contains("ludwika zieleniewskiego");
        Assertions.assertThat(streets.get(StreetType.STREET)).contains("ludwika zieleniewskiego");
        Assertions.assertThat(streets.get(StreetType.PARK)).contains("krowoderski");
        Assertions.assertThat(streets.get(StreetType.RESIDENTIAL)).contains("krowodrza górka");


    }


}