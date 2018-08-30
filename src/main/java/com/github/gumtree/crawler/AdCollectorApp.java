package com.github.gumtree.crawler;

import com.github.gumtree.crawler.adparsers.BatchAdInfoCollector;
import com.github.gumtree.crawler.adparsers.CompositeAdLinksCollector;
import com.github.gumtree.crawler.model.AdLinks;
import com.github.gumtree.crawler.nominatim.model.AdvertSearchSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.SECONDS;


@SpringBootApplication
public class AdCollectorApp implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdCollectorApp.class);
    @Autowired
    private CompositeAdLinksCollector compositeLinksCollector;
    @Autowired
    private BatchAdInfoCollector batchAdInfoCollector;
    @Value("${advers.fetcher.delay.seconds:300}")
    private int advertsFetcherDelay;

    public static void main(String[] args) {
        new SpringApplication(AdCollectorApp.class).run(args);
    }

    @Override
    public void run(String... args) {

        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(() -> {

            log.info("Fetching link adverts started");
            AdvertSearchSpec advertSearchSpec = new AdvertSearchSpec("Kraków", "Poland",
                    "https://www.gumtree.pl/s-mieszkania-i-domy-sprzedam-i-kupie/krakow/v1c9073l3200208p1",
                    "https://www.olx.pl/nieruchomosci/mieszkania/sprzedaz/krakow/",
                    "https://www.otodom.pl/sprzedaz/mieszkanie/krakow/?search%5Bdist%5D=0&search%5Bsubregion_id%5D=410&search%5Bcity_id%5D=38");
            AdLinks advertLinks = compositeLinksCollector.getAdvertLinks(advertSearchSpec, 1);

            batchAdInfoCollector.collectAdvertsDetails(advertLinks);
            log.info("Fetching link adverts finished");
        }, 0, advertsFetcherDelay, TimeUnit.SECONDS);
        try {
            System.out.println("Finish initialization, waiting for key press.");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
