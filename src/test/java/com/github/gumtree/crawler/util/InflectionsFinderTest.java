package com.github.gumtree.crawler.util;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class InflectionsFinderTest {

    @Test
    void findInflections() {
        InflectionsFinder inflectionsFinder = new InflectionsFinder();
        List<String> borsucza = inflectionsFinder.findInflections("Borsucza");
        Assertions.assertThat(borsucza).isNotEmpty().hasSize(20);
        Assertions.assertThat(borsucza).contains("borsuczej");
    }

    @Test
    void findInflections_shouldFindFirstWordInDictionary() {
        InflectionsFinder inflectionsFinder = new InflectionsFinder();
        List<String> borsucza = inflectionsFinder.findInflections("Borski");
        Assertions.assertThat(borsucza).isNotEmpty().hasSize(11);
        Assertions.assertThat(borsucza).contains("borskiej");
    }

    @Test
    void findInflections_shouldFindLastWordInDictionary() {
        InflectionsFinder inflectionsFinder = new InflectionsFinder();
        List<String> borsucza = inflectionsFinder.findInflections("Starowicom");
        Assertions.assertThat(borsucza).isNotEmpty().hasSize(5);
        Assertions.assertThat(borsucza).contains("starowice");
    }
}