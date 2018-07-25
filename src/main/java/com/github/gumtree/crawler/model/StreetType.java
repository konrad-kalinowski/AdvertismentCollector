package com.github.gumtree.crawler.model;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum StreetType {
    STREET("ulica", "ul.", "ul", "ulica", "ulicy"),
    AVENUE("aleje", "aleja", "al.", "al", "alejach", "aleji", "alei"),
    RESIDENTIAL("osiedle", "os.", "os", "osiedlu"),
    PARK("park", "parku"),
    SQUARE("plac", "placu", "pl.", "pl"),
    ROUNDABOUT("rondo", "rondzie", "ronda"),
    OTHER("");

    private String name;
    private Set<String> synonyms;

    StreetType(String name, String... synonyms) {
        this.name = name;
        List<String> synonimList = new ArrayList<>();
        synonimList.addAll(Arrays.asList(synonyms));
        synonimList.add(name);
        this.synonyms = Sets.newHashSet(synonimList);
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
