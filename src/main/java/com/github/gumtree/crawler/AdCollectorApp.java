package com.github.gumtree.crawler;

import com.github.gumtree.crawler.adparsers.BatchAdInfoCollector;
import com.github.gumtree.crawler.adparsers.CompsiteAdLinksCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class AdCollectorApp implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdCollectorApp.class);
    @Autowired
    private CompsiteAdLinksCollector compositeLinksCollector;
    @Autowired
    private BatchAdInfoCollector batchAdInfoCollector;

    public static void main(String[] args) {
        new SpringApplication(AdCollectorApp.class).run(args);
    }

    @Override
    public void run(String... args){
        List<String> advertsLinks = compositeLinksCollector.getAdvertLinks(
                "https://www.gumtree.pl/s-mieszkania-i-domy-sprzedam-i-kupie/krakow/v1c9073l3200208p1",
                1);
        advertsLinks.addAll(compositeLinksCollector.getAdvertLinks(
                "https://www.olx.pl/nieruchomosci/mieszkania/sprzedaz/krakow/",
                1));
        advertsLinks.addAll(compositeLinksCollector.getAdvertLinks(
                "https://www.otodom.pl/sprzedaz/mieszkanie/krakow/?search%5Bdescription%5D=1&search%5Bdist%5D=" +
                        "0&search%5Bsubregion_id%5D=410&search%5Bcity_id%5D=38",
                1));

        batchAdInfoCollector.collectAdvertsDetails(advertsLinks);
        try {
            System.out.println("Finish initialization, waiting for key press.");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
