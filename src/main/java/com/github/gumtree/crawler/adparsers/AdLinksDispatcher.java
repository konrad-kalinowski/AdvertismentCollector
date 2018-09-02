package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.model.AdLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class AdLinksDispatcher {
    private static final Logger log = LoggerFactory.getLogger(AdLinksDispatcher.class);

    private final BlockingQueue<AdLink> allAdLinksQueue;
    private final BlockingQueue<AdLink> olxAdLinksQueue;
    private final BlockingQueue<AdLink> gumtreeAdLinksQueue;
    private final BlockingQueue<AdLink> otodomAdLinksQueue;

    @Autowired
    public AdLinksDispatcher(@Qualifier("adLinksQueue") LinkedBlockingQueue<AdLink> allAdLinksQueue,
                             @Qualifier("olxAdLinksQueue") LinkedBlockingQueue<AdLink> olxAdLinksQueue,
                             @Qualifier("gumtreeAdLinksQueue") LinkedBlockingQueue<AdLink> gumtreeAdLinksQueue,
                             @Qualifier("otoDomAdLinksQueue") LinkedBlockingQueue<AdLink> otoDomAdLinksQueue) {
        this.allAdLinksQueue = allAdLinksQueue;
        this.olxAdLinksQueue = olxAdLinksQueue;
        this.gumtreeAdLinksQueue = gumtreeAdLinksQueue;
        this.otodomAdLinksQueue = otoDomAdLinksQueue;
    }

    @PostConstruct
    public void scheduleLinksDispatch() {
        new Thread(() -> {
            while (true) {
                try {
                    AdLink adLink = allAdLinksQueue.take();
                    String link = adLink.getLink();
                    if (link.startsWith(Domain.OLX_DOMAIN)) {
                        olxAdLinksQueue.offer(adLink);
                    } else if (link.startsWith(Domain.GUMTREE_DOMAIN)) {
                        gumtreeAdLinksQueue.offer(adLink);
                    } else if (link.startsWith(Domain.OTODOM_DOMAIN)) {
                        otodomAdLinksQueue.offer(adLink);
                    } else {
                        log.warn("Unrecognized link {}", link);
                    }
                } catch (InterruptedException e) {
                    log.error("AdLinksDispatcher interrupted.");
                    return;
                }
            }
        }).start();
    }


}
