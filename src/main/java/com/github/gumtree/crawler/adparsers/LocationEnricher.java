package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.db.AdvertsUploader;
import com.github.gumtree.crawler.model.Advertisement;
import com.github.gumtree.crawler.model.Coordinates;
import com.github.gumtree.crawler.model.StreetType;
import com.github.gumtree.crawler.nominatim.NominatinClient;
import com.github.gumtree.crawler.nominatim.model.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

@Service
public class LocationEnricher {

    private static final Logger log = LoggerFactory.getLogger(LocationEnricher.class);

    private final LocationFinder locationFinder;
    private final AdvertsUploader advertsUploader;
    private Map<StreetType, Set<String>> availableStreets;
    private NominatinClient nominatinClient;
    private final BlockingQueue<Advertisement> advertsQueue;


    @Autowired
    public LocationEnricher(LocationFinder locationFinder,
                            AdvertsUploader advertsUploader,
                            Map<StreetType, Set<String>> availableStreets,
                            NominatinClient nominatinClient,
                            BlockingQueue<Advertisement> advertsQueue) {
        this.locationFinder = locationFinder;
        this.advertsUploader = advertsUploader;
        this.availableStreets = availableStreets;
        this.nominatinClient = nominatinClient;
        this.advertsQueue = advertsQueue;
    }

    @PostConstruct
    public void scheduleAdvertismentEnricher() {
        new Thread(() -> {
            try {
                while (true) {
                    Advertisement advertisement = advertsQueue.take();
                    enrichAndSaveAdvert(advertisement);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "Location-Enricher").start();
    }

    public void enrichAndSaveAdvert(Advertisement advertisement) {
        Set<String> locationInDesc = locationFinder.findLocationInDesc(availableStreets, advertisement.getDescription());
        advertisement.setPossibleAddresses(locationInDesc);
        if (!advertisement.getStreets().isEmpty()) {
            String street = advertisement.getStreets().iterator().next();
            List<SearchResult> coordinates = nominatinClient.findCoordinates(street, advertisement.getCity(), advertisement.getCountry());
            advertisement.setCoodrinates(new Coordinates(coordinates.get(0).getLongtitude(), coordinates.get(0).getLatitude()));
        } else {
            advertisement.setCoodrinates(Coordinates.EMPTY_COORDINATES);
        }
        advertsUploader.uploadAdverts(Collections.singletonList(advertisement));
    }
}