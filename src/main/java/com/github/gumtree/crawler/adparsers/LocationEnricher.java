package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.db.AdvertsUploader;
import com.github.gumtree.crawler.model.Advertisement;
import com.github.gumtree.crawler.model.StreetType;
import com.github.gumtree.crawler.nominatim.NominatinClient;
import com.github.gumtree.crawler.nominatim.model.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LocationEnricher implements OnBatchReadyListener {

    private static final Logger log = LoggerFactory.getLogger(LocationEnricher.class);

    private final LocationFinder locationFinder;
    private final AdvertsUploader advertsUploader;
    private Map<StreetType, Set<String>> availableStreets;
    private NominatinClient nominatinClient;


    @Autowired
    public LocationEnricher(LocationFinder locationFinder, AdvertsUploader advertsUploader, Map<StreetType, Set<String>> availableStreets, NominatinClient nominatinClient) {
        this.locationFinder = locationFinder;
        this.advertsUploader = advertsUploader;
        this.availableStreets = availableStreets;
        this.nominatinClient = nominatinClient;
    }

    @Override
    public void onBatchReady(List<Advertisement> batch) {

        for (Advertisement advertisement : batch) {
            Set<String> locationInDesc = locationFinder.findLocationInDesc(availableStreets, advertisement.getDescription());
            advertisement.setPossibleAddresses(locationInDesc);
            if (!advertisement.getStreets().isEmpty()) {
                String street = advertisement.getStreets().iterator().next();
                List<SearchResult> coordinates = nominatinClient.findCoordinates(street, advertisement.getCity(), advertisement.getCountry());
                advertisement.setCoodrinates(coordinates.get(0).getLatitude(), coordinates.get(0).getLongtitude());
            }
        }
        advertsUploader.uploadAdverts(batch);
    }
}