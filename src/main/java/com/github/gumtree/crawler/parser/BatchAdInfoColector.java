package com.github.gumtree.crawler.parser;

import com.github.gumtree.crawler.model.Advertisement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BatchAdInfoColector {

    private static final Logger log = LoggerFactory.getLogger(BatchAdInfoColector.class);
    private final AdInfoCollector adInfoCollector;

    public BatchAdInfoColector(AdInfoCollector adInfoCollector) {
        this.adInfoCollector = adInfoCollector;
    }

    public List<Advertisement> collectAdvertsDetails(List<String> advertLinks, int inactivePeriodOfSeconds) {
        List<Advertisement> adverts = new ArrayList<>();
        try {
            for (String advertLink : advertLinks) {
                log.debug("Fetching adverisment info {}", advertLink);
                Advertisement advertisement = adInfoCollector.collectAdInfo(advertLink);
                adverts.add(advertisement);
                try {
                    Thread.sleep(1000 * inactivePeriodOfSeconds);
                } catch (InterruptedException e) {
                    log.error("Sleep interrupted", e);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to Fetch advert", e);
        }
        return adverts;
    }
}
