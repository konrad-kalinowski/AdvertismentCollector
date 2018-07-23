package com.github.gumtree.crawler;

import com.github.gumtree.crawler.adparsers.AdLinksCollector;
import com.github.gumtree.crawler.adparsers.BatchAdInfoCollector;
import com.github.gumtree.crawler.adparsers.CompsiteAdLinksCollector;
import com.github.gumtree.crawler.adparsers.DuplicatedLinkChecker;
import com.github.gumtree.crawler.adparsers.JsoupProvider;
import com.github.gumtree.crawler.adparsers.LocationEnricher;
import com.github.gumtree.crawler.adparsers.LocationFinder;
import com.github.gumtree.crawler.adparsers.gumtree.AdInfoCollectorGumTree;
import com.github.gumtree.crawler.adparsers.gumtree.AdListLinkCollectorGumTree;
import com.github.gumtree.crawler.adparsers.olx.AdInfoCollectorOlx;
import com.github.gumtree.crawler.adparsers.olx.AdListLinkCollectorOlx;
import com.github.gumtree.crawler.adparsers.oto_dom.AdListLinkCollectorOtoDom;
import com.github.gumtree.crawler.db.AdCollectorDao;
import com.github.gumtree.crawler.db.AdvertsUploader;
import com.github.gumtree.crawler.util.InflectionsFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class AdCollectorApp {

    private static final Logger log = LoggerFactory.getLogger(AdCollectorApp.class);
    public static final int INACTIVE_PERIOD_SECONDS = 1;


    public static void main(String[] args) {
        String importFile = "";
        for (int i = 0; args != null && i < args.length; i++) {
            String a = args[i];
            if ("-import".equals(a)) {
                importFile = args[++i];
            }
        }

        AdCollectorDao adCollectorDao = null;
        try {
            adCollectorDao = new AdCollectorDao();
            if (!importFile.isEmpty()) {
                adCollectorDao.initialize(importFile);
            } else {
                JsoupProvider jsoupProvider = new JsoupProvider();
                adCollectorDao.initialize();
                DuplicatedLinkChecker duplicatedLinkChecker = new DuplicatedLinkChecker(adCollectorDao.getStoredLinks());
                AdLinksCollector adListLinkCollectorGumTree = new AdListLinkCollectorGumTree(jsoupProvider, duplicatedLinkChecker);
                AdLinksCollector adListLinkCollectorOlx = new AdListLinkCollectorOlx(jsoupProvider, duplicatedLinkChecker);
                AdListLinkCollectorOtoDom adListLinkCollectorOtoDom = new AdListLinkCollectorOtoDom(jsoupProvider, duplicatedLinkChecker);
                CompsiteAdLinksCollector compositeLinksCollector = new CompsiteAdLinksCollector(jsoupProvider,
                        duplicatedLinkChecker,
                        Arrays.asList(adListLinkCollectorGumTree, adListLinkCollectorOlx, adListLinkCollectorOtoDom));

                List<String> advertsLinks = compositeLinksCollector.getAdvertLinks(
                        "https://www.gumtree.pl/s-mieszkania-i-domy-sprzedam-i-kupie/krakow/v1c9073l3200208p1",
                        1, INACTIVE_PERIOD_SECONDS);
                advertsLinks.addAll(compositeLinksCollector.getAdvertLinks(
                        "https://www.olx.pl/nieruchomosci/mieszkania/krakow/",
                        1, INACTIVE_PERIOD_SECONDS));
                advertsLinks.addAll(compositeLinksCollector.getAdvertLinks(
                        "https://www.otodom.pl/wynajem/mieszkanie/krakow/?search%5Bdescription%5D=" +
                                "1&search%5Bdist%5D=0&search%5Bsubregion_id%5D=410&search%5Bcity_id%5D=38",
                        1, INACTIVE_PERIOD_SECONDS));


                AdvertsUploader advertsUploader = new AdvertsUploader(adCollectorDao);
                StreetNamesProvider streetNamesProvider = new StreetNamesProvider();
                String city = "Kraków";
                int citySymbolInSincDB = streetNamesProvider.findCitySymbolInSincDB(12, 61, 05, city);
                log.debug("{} city symbol = {}", city, citySymbolInSincDB);
                Set<String> streets = streetNamesProvider.findStreets(citySymbolInSincDB);
                log.debug("Initialized with {} streets {}", streets.size(), streets);

                LocationEnricher locationEnricher = new LocationEnricher(new LocationFinder(streets, new InflectionsFinder()), advertsUploader);
                BatchAdInfoCollector batchAdInfoCollector = new BatchAdInfoCollector(
                        Arrays.asList(new AdInfoCollectorGumTree(jsoupProvider), new AdInfoCollectorOlx(jsoupProvider)),
                        jsoupProvider, Arrays.asList(locationEnricher, duplicatedLinkChecker), 10);
                batchAdInfoCollector.collectAdvertsDetails(advertsLinks, INACTIVE_PERIOD_SECONDS);
            }
            try {
                System.out.println("Finish initialization, waiting for key press.");
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            if (adCollectorDao != null) {
                adCollectorDao.close();
            }
        }
    }
}