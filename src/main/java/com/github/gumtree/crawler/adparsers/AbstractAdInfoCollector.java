package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.adparsers.gumtree.AdInfoCollectorGumTree;
import com.github.gumtree.crawler.model.AdLink;
import com.github.gumtree.crawler.model.Advertisement;
import com.google.common.util.concurrent.Uninterruptibles;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class AbstractAdInfoCollector implements AdInfoCollector {
    private static final Logger log = LoggerFactory.getLogger(AdInfoCollectorGumTree.class);
    private final JsoupProvider jsoupProvider;
    private final BlockingQueue<AdLink> adLinkBlockingQueue;
    private final BlockingQueue<Advertisement> advertisementQueue;
    private final int inactivityPeriodSeconds;

    public AbstractAdInfoCollector(JsoupProvider jsoupProvider,
                                   BlockingQueue<AdLink> adLinkBlockingQueue,
                                   BlockingQueue<Advertisement> advertisementQueue,
            int inactivityPeriodSeconds) {
        this.jsoupProvider = jsoupProvider;
        this.adLinkBlockingQueue = adLinkBlockingQueue;
        this.advertisementQueue = advertisementQueue;
        this.inactivityPeriodSeconds = inactivityPeriodSeconds;
    }

    public void scheduleCollectingAdInfo(String name) {
        new Thread(() -> {

            try {
                while (true) {
                    AdLink adLink = adLinkBlockingQueue.take();
                    Advertisement advertisement = collectAdInfo(adLink.getCountry(),
                            adLink.getCity(),
                            jsoupProvider.connect(adLink.getLink()));
                    log.debug("Collected advertisement info for url {}", adLink.getLink());
                    advertisementQueue.offer(advertisement);
                    Uninterruptibles.sleepUninterruptibly(inactivityPeriodSeconds, TimeUnit.SECONDS);
                }
            } catch (InterruptedException e) {
                log.error("Gumtree adInfoCollector interrupted");
            }
        }, name).start();

    }

    public Advertisement collectInfo(File file) {
        Document document = jsoupProvider.parseFile(file);
        return collectAdInfo("", "", document);

    }
}
