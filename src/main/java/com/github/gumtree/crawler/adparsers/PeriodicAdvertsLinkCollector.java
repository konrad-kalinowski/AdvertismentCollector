package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.model.AdLinks;
import com.github.gumtree.crawler.nominatim.model.AdvertSearchSpec;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class PeriodicAdvertsLinkCollector {
    private static final Logger log = LoggerFactory.getLogger(PeriodicAdvertsLinkCollector.class);
    private final CompositeAdLinksCollector compositeAdLinksCollector;
    private final long advertsFetcherDelay;

    @Autowired
    public PeriodicAdvertsLinkCollector(CompositeAdLinksCollector compositeAdLinksCollector,
                                        @Value("${adverts.fetcher.delay.seconds:300}") long advertsFetcherDelay) {
        this.compositeAdLinksCollector = compositeAdLinksCollector;
        this.advertsFetcherDelay = advertsFetcherDelay;
    }

    @PostConstruct
    public void initializeFetchingAdvertLinks() {
        Executors.newScheduledThreadPool(1,
                new ThreadFactoryBuilder()
                        .setNameFormat("links-collector-%s")
                        .build())
                .scheduleWithFixedDelay(() -> {

            log.info("Fetching link adverts started");
            AdvertSearchSpec advertSearchSpec = new AdvertSearchSpec("Kraków", "Poland",
                    "https://www.gumtree.pl/s-mieszkania-i-domy-sprzedam-i-kupie/krakow/v1c9073l3200208p1",
                    "https://www.olx.pl/nieruchomosci/mieszkania/sprzedaz/krakow/",
                    "https://www.otodom.pl/sprzedaz/mieszkanie/krakow/?search%5Bdist%5D=0&search%5Bsubregion_id%5D=410&search%5Bcity_id%5D=38");
            compositeAdLinksCollector.getAdvertLinks(advertSearchSpec, 1);

            log.info("Fetching link adverts finished");
        }, 0, advertsFetcherDelay, TimeUnit.SECONDS);
    }


}
