package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.adparsers.gumtree.AdInfoCollectorGumTree;
import com.github.gumtree.crawler.model.AdLink;
import com.github.gumtree.crawler.model.Advertisement;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public abstract class AbstractAdInfoCollector implements AdInfoCollector {
    private static final Logger log = LoggerFactory.getLogger(AdInfoCollectorGumTree.class);
    private final JsoupProvider jsoupProvider;
    private final BlockingQueue<AdLink> adLinkBlockingQueue;
    private final BlockingQueue<Advertisement> advertisementQueue;

    public AbstractAdInfoCollector(JsoupProvider jsoupProvider,
                                   BlockingQueue<AdLink> adLinkBlockingQueue,
                                   BlockingQueue<Advertisement> advertisementQueue) {
        this.jsoupProvider = jsoupProvider;
        this.adLinkBlockingQueue = adLinkBlockingQueue;
        this.advertisementQueue = advertisementQueue;
    }

    public void scheduleCollectingAdInfo(String name) {
        new Thread(() -> {

            try {
                while (true) {
                    AdLink adLink = adLinkBlockingQueue.take();
                    Advertisement advertisement = collectAdInfo(adLink.getCountry(),
                            adLink.getCity(),
                            jsoupProvider.connect(adLink.getLink()));
                    advertisementQueue.offer(advertisement);

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
