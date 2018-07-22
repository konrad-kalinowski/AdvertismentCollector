package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.db.AdvertsUploader;
import com.github.gumtree.crawler.model.Advertisement;

import java.util.List;
import java.util.Set;

public class LocationEnricher implements OnBatchReadyListener {

    private final LocationFinder locationFinder;
    private final AdvertsUploader advertsUploader;

    public LocationEnricher(LocationFinder locationFinder, AdvertsUploader advertsUploader) {
        this.locationFinder = locationFinder;
        this.advertsUploader = advertsUploader;
    }

    @Override
    public void onBatchReady(List<Advertisement> batch) {
        for (Advertisement advertisement : batch) {
            Set<String> locationInDesc = locationFinder.findLocationInDesc(advertisement.getDescription());
            advertisement.setpossibleaddresses(locationInDesc);
        }
        advertsUploader.uploadAdverts(batch);
    }
}
