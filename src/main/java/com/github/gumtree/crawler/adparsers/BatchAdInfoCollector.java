package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.model.Advertisement;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BatchAdInfoCollector {

    private static final Logger log = LoggerFactory.getLogger(BatchAdInfoCollector.class);
    private final AdInfoCollector adInfoCollector;
    private final JsoupProvider jsoupProvider;
    private final List<OnBatchReadyListener> batchReadyListeners;
    private final int batchSize;

    public BatchAdInfoCollector(AdInfoCollector adInfoCollector, JsoupProvider jsoupProvider, List<OnBatchReadyListener> batchReadyListeners, int batchSize) {
        this.adInfoCollector = adInfoCollector;
        this.jsoupProvider = jsoupProvider;
        this.batchReadyListeners = batchReadyListeners;
        this.batchSize = batchSize;
    }

    public void collectAdvertsDetails(List<String> advertLinks, int inactivePeriodOfSeconds) {
        List<List<String>> partitions = Lists.partition(advertLinks, batchSize);

        for (List<String> partition : partitions) {
            List<Advertisement> advertBatch = new ArrayList<>();
            for (String advertLink : partition) {
                try {
                    log.debug("Fetching adverisment info {}", advertLink);
                    Advertisement advertisement = adInfoCollector.collectAdInfo(jsoupProvider.connect(advertLink));
                    advertBatch.add(advertisement);
                    Uninterruptibles.sleepUninterruptibly(inactivePeriodOfSeconds, TimeUnit.SECONDS);
                } catch (Exception e) {
                    log.warn("Failed to fetch advert " + advertLink, e);
                }
            }
            log.info("Notifying listeners - new batch size = {}", advertBatch.size());
            batchReadyListeners.forEach(listener -> listener.onBatchReady(advertBatch));
        }
    }
}
