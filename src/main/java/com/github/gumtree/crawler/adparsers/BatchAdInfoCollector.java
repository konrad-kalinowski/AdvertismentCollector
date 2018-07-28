package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.model.AdLinks;
import com.github.gumtree.crawler.model.Advertisement;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BatchAdInfoCollector {

    private static final Logger log = LoggerFactory.getLogger(BatchAdInfoCollector.class);
    private final List<AdInfoCollector> adInfoCollectors;
    private final JsoupProvider jsoupProvider;
    private final List<OnBatchReadyListener> batchReadyListeners;
    private final int batchSize;
    private int inactivePeriodSeconds;

    @Autowired
    public BatchAdInfoCollector(List<AdInfoCollector> adInfoCollectors,
                                JsoupProvider jsoupProvider,
                                List<OnBatchReadyListener> batchReadyListeners,
                                @Value("${adverts.collector.batch.size:1}") int batchSize,
                                @Value("${ad.links.collector.inactive.period.seconds:5}") int inactivePeriodSeconds) {
        this.adInfoCollectors = adInfoCollectors;
        this.jsoupProvider = jsoupProvider;
        this.batchReadyListeners = batchReadyListeners;
        this.batchSize = batchSize;
        this.inactivePeriodSeconds = inactivePeriodSeconds;
    }

    public void collectAdvertsDetails(AdLinks adLinks) {
        List<List<String>> partitions = Lists.partition(adLinks.getLinks(), batchSize);

        for (List<String> partition : partitions) {
            List<Advertisement> advertBatch = new ArrayList<>();
            for (String advertLink : partition) {
                try {
                    log.debug("Fetching adverisment info {}", advertLink);
                    AdInfoCollector adInfoCollector = this.adInfoCollectors.stream()
                            .filter(collector -> collector.canProcess(advertLink))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Failed to find suitable advert collector for link " + advertLink));
                    Advertisement advertisement = adInfoCollector.collectAdInfo(adLinks.getCountry(), adLinks.getCity(), jsoupProvider.connect(advertLink));
                    advertBatch.add(advertisement);
                    Uninterruptibles.sleepUninterruptibly(inactivePeriodSeconds, TimeUnit.SECONDS);
                } catch (Exception e) {
                    log.warn("Failed to fetch advert " + advertLink, e);
                }
            }
            log.info("Notifying listeners - new batch size = {}", advertBatch.size());
            batchReadyListeners.forEach(listener -> listener.onBatchReady(advertBatch));
        }
    }
}
