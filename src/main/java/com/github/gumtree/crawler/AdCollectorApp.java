package com.github.gumtree.crawler;

import com.github.gumtree.crawler.db.AdCollectorDao;
import com.github.gumtree.crawler.model.Advertisement;
import com.github.gumtree.crawler.parser.AdInfoCollector;
import com.github.gumtree.crawler.parser.AdListLinkCollector;
import com.github.gumtree.crawler.parser.BatchAdInfoColector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class AdCollectorApp {

    private static final Logger log = LoggerFactory.getLogger(AdCollectorApp.class);
    public static final int INACTIVE_PERIOD_SECONDS = 1;


    public static void main(String[] args) throws InterruptedException {

        AdListLinkCollector adListLinkCollector = new AdListLinkCollector();
        List<String> advertsLinks = adListLinkCollector.getAdvertsLinks("https://www.gumtree.pl/s-mieszkania-i-domy-sprzedam-i-kupie/" +
                "krakow/v1c9073l3200208p1", 1, INACTIVE_PERIOD_SECONDS);
        BatchAdInfoColector batchAdInfoColector = new BatchAdInfoColector(new AdInfoCollector());
        List<Advertisement> advertisements = batchAdInfoColector.collectAdvertsDetails(advertsLinks, INACTIVE_PERIOD_SECONDS);
        AdCollectorDao adCollectorDao = new AdCollectorDao();
        adCollectorDao.initialize();
        adCollectorDao.addAdverts(advertisements);
        Thread.sleep(60*60*1000);
        adCollectorDao.close();

    }
}