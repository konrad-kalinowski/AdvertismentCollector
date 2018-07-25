package com.github.gumtree.crawler;

import com.github.gumtree.crawler.model.StreetType;
import com.google.common.collect.Sets;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StreetNamesProvider {
    private static final Logger log = LoggerFactory.getLogger(StreetNamesProvider.class);


    public Set<Integer> findCitySymbolInSincDB(int voivodeshipNumber, int countyNumber, String city) {
        InputStream adressResource = StreetNamesProvider.class.getResourceAsStream("/SIMC_Adresowy_2018-07-18.csv");
        try (Reader reader = new InputStreamReader(new BOMInputStream(adressResource), StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader()
                    .withQuote(null)
                    .withTrim()
                    .withDelimiter(';')
                    .parse(reader);
            Set<Integer> citysymbols = new HashSet<>();
            for (CSVRecord record : records) {
                int voivodeship = Integer.parseInt(record.get("WOJ"));
                int county = Integer.parseInt(record.get("POW"));
                int citySymbol = Integer.parseInt(record.get("SYM"));
                String cityRecord = record.get("NAZWA");
                if (voivodeship == voivodeshipNumber && county == countyNumber && cityRecord.startsWith(city)) {
                    log.debug("Found city {} for phrase {}", cityRecord, city);
                    citysymbols.add(citySymbol);
                }

            }
            return citysymbols;

        } catch (IOException e) {
            log.error("Failed to read file", e);
        }
        throw new IllegalArgumentException("Did not find any city for given args.");
    }

    public Map<StreetType, Set<String>> findStreets(int citySymbol) {
        Map<StreetType, Set<String>> streetTypeToName = new HashMap<>();
        InputStream addressResource = StreetNamesProvider.class.getResourceAsStream("/ULIC_Adresowy_2018-07-18.csv");
        try (Reader reader = new InputStreamReader(addressResource, StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader()
                    .withQuote(null)
                    .withTrim()
                    .withDelimiter(';')
                    .parse(reader);

            for (CSVRecord record : records) {
                int citySym = Integer.parseInt(record.get("SYM"));
                if (citySym == citySymbol) {
                    String streetTypeString = record.get("CECHA");
                    String streetFirstPart = (record.get("NAZWA_1"));
                    String secondStreetNamePart = record.get("NAZWA_2");
                    String fullStreetName = buildFullStreetName(streetFirstPart, secondStreetNamePart);

                    StreetType streetType;
                    if (StringUtils.isBlank(streetTypeString)) {
                        List<String> words = Arrays.asList(fullStreetName.split(" "));
                        streetType = StreetType.findStreetType(words.get(0));
                        if (streetType != StreetType.OTHER) {
                            fullStreetName = String.join(" ", words.subList(1, words.size()));
                        }
                    } else {
                        streetType = StreetType.findStreetType(streetTypeString);
                    }

                    if (!streetTypeToName.containsKey(streetType)) {
                        Set<String> streets = new HashSet<>();
                        streetTypeToName.put(streetType, streets);
                    }
                    streetTypeToName.get(streetType).add(fullStreetName);
                }

            }
        } catch (IOException e) {
            log.error("ERRROR", e);
        }

        for (Map.Entry<StreetType, Set<String>> entry : streetTypeToName.entrySet()) {
            log.debug("Found {} streets for street type {}", entry.getValue().size(), entry.getKey());

        }
        return streetTypeToName;

    }

    private String buildFullStreetName(String streetFirstPart, String secondStreetNamePart) {
        StringBuilder streetNameBuilder = new StringBuilder();
        if (!secondStreetNamePart.isEmpty()) {
            streetNameBuilder.append(secondStreetNamePart).append(" ");
        }
        streetNameBuilder.append(streetFirstPart);
        return streetNameBuilder.toString().toLowerCase();
    }

    public Map<StreetType, Set<String>> findStreets(Set<Integer> citySymbols) {
        return citySymbols.stream()
                .map(symbol -> findStreets(symbol))
                .flatMap(streets -> streets.entrySet().stream())
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue(), (v1, v2) -> Sets.union(v1, v2)));


    }


}

