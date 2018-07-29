package com.github.gumtree.crawler;

import com.github.gumtree.crawler.adparsers.BatchAdInfoCollector;
import com.github.gumtree.crawler.adparsers.CompositeAdLinksCollector;
import com.github.gumtree.crawler.model.AdLinks;
import com.github.gumtree.crawler.nominatim.model.AdvertSearchSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class AdCollectorApp implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdCollectorApp.class);
    @Autowired
    private CompositeAdLinksCollector compositeLinksCollector;
    @Autowired
    private BatchAdInfoCollector batchAdInfoCollector;

    public static void main(String[] args) {
        new SpringApplication(AdCollectorApp.class).run(args);
    }

    @Override
    public void run(String... args) {
        AdvertSearchSpec advertSearchSpec = new AdvertSearchSpec("Kraków", "Poland",
                "https://www.gumtree.pl/s-mieszkania-i-domy-sprzedam-i-kupie/krakow/v1c9073l3200208p1",
                "https://www.gumtree.pl/s-mieszkania-i-domy-sprzedam-i-kupie/krakow/v1c9073l3200208p1",
                "https://www.gumtree.pl/s-mieszkania-i-domy-sprzedam-i-kupie/krakow/v1c9073l3200208p1");
        AdLinks advertLinks = compositeLinksCollector.getAdvertLinks(advertSearchSpec, 1);

        batchAdInfoCollector.collectAdvertsDetails(advertLinks);
        try {
            System.out.println("Finish initialization, waiting for key press.");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
