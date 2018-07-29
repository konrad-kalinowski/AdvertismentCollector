package com.github.gumtree.crawler.nominatim;

import com.github.gumtree.crawler.nominatim.model.SearchResult;
import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class NominatinClient {

    private static final Logger log = LoggerFactory.getLogger(NominatinClient.class);

    private final NominatimService nominatimService;

    @Autowired
    public NominatinClient(NominatimService nominatimService) {
        this.nominatimService = nominatimService;
    }

    public List<SearchResult> findCoordinates(String street, String city, String country) {
        try {
            log.debug("Looking for coordinates for {}", street, city, country);
            List<SearchResult> json = nominatimService.findCoordinates("json", street, city, country).execute().body();
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            log.debug("Found {} results", json.size());
            return json;
        } catch (IOException e) {
            log.error("Failed to find coordinates", e);
            return Collections.emptyList();
        }
    }
}
