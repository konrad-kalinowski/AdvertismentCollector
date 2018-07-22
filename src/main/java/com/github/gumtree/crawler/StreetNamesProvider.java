package com.github.gumtree.crawler;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StreetNamesProvider {
    private static final Logger log = LoggerFactory.getLogger(StreetNamesProvider.class);


    public int findCitySymbolInSincDB(int voivodeshipNumber, int countyNumber, int communeNumber, String city) {
        InputStream adressResource = StreetNamesProvider.class.getResourceAsStream("/SIMC_Adresowy_2018-07-18.csv");
        try (Reader reader = new InputStreamReader(new BOMInputStream(adressResource), StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader()
                    .withQuote(null)
                    .withTrim()
                    .withDelimiter(';')
                    .parse(reader);
            for (CSVRecord record : records) {
                int voivodeship = Integer.parseInt(record.get("WOJ"));
                int county = Integer.parseInt(record.get("POW"));
                int commune = Integer.parseInt(record.get("GMI"));
                int citySymbol = Integer.parseInt(record.get("SYM"));
                String cityRecord = record.get("NAZWA");
                if (voivodeship == voivodeshipNumber && county == countyNumber && commune == communeNumber && cityRecord.equals(city)) {
                    return citySymbol;
                }
            }

        } catch (IOException e) {
            log.error("Failed to read file", e);
        }
        throw new IllegalArgumentException("Did not find any city for given args.");
    }

    public Set<String> findStreets(int citySymbol) {
        InputStream addressResource = StreetNamesProvider.class.getResourceAsStream("/ULIC_Adresowy_2018-07-18.csv");
        Set<String> streets = new HashSet<>();
        try (Reader reader = new InputStreamReader(addressResource, StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader()
                    .withQuote(null)
                    .withTrim()
                    .withDelimiter(';')
                    .parse(reader);
            for (CSVRecord record : records) {
                String street = (record.get("NAZWA_1"));
                int citySym = Integer.parseInt(record.get("SYM"));
                if(!record.get("NAZWA_2").isEmpty()){
                    street = record.get("NAZWA_2") + " " + street;
                }

                if (citySym == citySymbol) {
                    streets.add(street.toLowerCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return streets;

    }
}

