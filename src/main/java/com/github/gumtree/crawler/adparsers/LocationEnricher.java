package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.db.AdvertsUploader;
import com.github.gumtree.crawler.model.Advertisement;
import com.github.gumtree.crawler.model.StreetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LocationEnricher implements OnBatchReadyListener {

    private final LocationFinder locationFinder;
    private final AdvertsUploader advertsUploader;
    private Map<StreetType, Set<String>> availableStreets;

    @Autowired
    public LocationEnricher(LocationFinder locationFinder, AdvertsUploader advertsUploader,Map<StreetType, Set<String>> availableStreets) {
        this.locationFinder = locationFinder;
        this.advertsUploader = advertsUploader;
        this.availableStreets = availableStreets;
    }

    @Override
    public void onBatchReady(List<Advertisement> batch) {
        for (Advertisement advertisement : batch) {
            Set<String> locationInDesc = locationFinder.findLocationInDesc(availableStreets,advertisement.getDescription());
            advertisement.setpossibleaddresses(locationInDesc);
        }
        advertsUploader.uploadAdverts(batch);
    }
}