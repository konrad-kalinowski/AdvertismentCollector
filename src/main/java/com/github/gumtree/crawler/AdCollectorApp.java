package com.github.gumtree.crawler;

import com.github.gumtree.crawler.adparsers.BatchAdInfoCollector;
import com.github.gumtree.crawler.adparsers.DuplicatedLinkChecker;
import com.github.gumtree.crawler.adparsers.JsoupProvider;
import com.github.gumtree.crawler.adparsers.gumtree.AdInfoCollectorGumTree;
import com.github.gumtree.crawler.adparsers.gumtree.AdListLinkCollectorGumTree;
import com.github.gumtree.crawler.db.AdCollectorDao;
import com.github.gumtree.crawler.db.AdvertsUploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


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
                AdListLinkCollectorGumTree adListLinkCollectorGumTree = new AdListLinkCollectorGumTree(jsoupProvider, duplicatedLinkChecker);
                List<String> advertsLinks = adListLinkCollectorGumTree.getAdvertLinks("https://www.gumtree.pl/s-mieszkania-i-domy-sprzedam-i-kupie/" +
                        "krakow/v1c9073l3200208p1", 2, INACTIVE_PERIOD_SECONDS);
                AdvertsUploader advertsUploader = new AdvertsUploader(adCollectorDao);
                BatchAdInfoCollector batchAdInfoCollector = new BatchAdInfoCollector(new AdInfoCollectorGumTree(jsoupProvider),
                        jsoupProvider, Arrays.asList(advertsUploader, duplicatedLinkChecker), 10);
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