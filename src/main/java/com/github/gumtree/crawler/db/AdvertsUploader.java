package com.github.gumtree.crawler.db;

import com.github.gumtree.crawler.adparsers.OnBatchReadyListener;
import com.github.gumtree.crawler.model.Advertisement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AdvertsUploader implements OnBatchReadyListener {
    private static final Logger log = LoggerFactory.getLogger(AdvertsUploader.class);
    private final AdCollectorDao adCollectorDao;

    public AdvertsUploader(AdCollectorDao adCollectorDao) {
        this.adCollectorDao = adCollectorDao;
    }

    @Override
    public void onBatchReady(List<Advertisement> batch) {
        log.info("Adding {} adverts to db", batch.size());
        adCollectorDao.addAdverts(batch);
    }
}
