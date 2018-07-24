package com.github.gumtree.crawler.model;

import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum StreetType {
    STREET("ul.", "ul", "ulica", "ulicy"),
    AVENUE("al.", "al", "aleje", "alejach", "aleji", "alei"),
    RESIDENTIAL("os.", "os", "osiedle", "osiedlu"),
    OTHER("");

    private Set<String> synonyms;

    StreetType(String... synonyms) {
        this.synonyms = Sets.newHashSet(synonyms);
    }

    public static StreetType findStreetType(String typeName) {
        return Arrays.stream(StreetType.values())
                .filter(streetType -> streetType.synonyms.contains(typeName.toLowerCase()))
                .findFirst()
                .orElse(OTHER);
    }

    public static Set<String> allStreetNamePrefixes() {
        return Arrays.stream(StreetType.values())
                .map(streetType -> streetType.synonyms)
                .flatMap(set -> set.stream())
                .collect(Collectors.toSet());
    }
}
