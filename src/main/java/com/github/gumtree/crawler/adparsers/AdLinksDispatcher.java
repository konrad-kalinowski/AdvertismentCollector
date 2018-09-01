package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.model.AdLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;

@Service
public class AdLinksDispatcher {
    private static final Logger log = LoggerFactory.getLogger(AdLinksDispatcher.class);

    private final BlockingQueue<AdLink> allAdLinksQueue;
    private final BlockingQueue<AdLink> olxAdLinksQueue;
    private final BlockingQueue<AdLink> gumtreeAdLinksQueue;
    private final BlockingQueue<AdLink> otodomAdLinksQueue;

    @Autowired
    public AdLinksDispatcher(@Qualifier("adLinksQueue") BlockingQueue<AdLink> allAdLinksQueue,
                             @Qualifier("olxAdLinksQueue") BlockingQueue<AdLink> olxAdLinksQueue,
                             @Qualifier("gumtreeAdLinksQueue") BlockingQueue<AdLink> gumtreeAdLinksQueue,
                             @Qualifier("otodomAdLinksQueue") BlockingQueue<AdLink> otodomAdLinksQueue) {
        this.allAdLinksQueue = allAdLinksQueue;
        this.olxAdLinksQueue = olxAdLinksQueue;
        this.gumtreeAdLinksQueue = gumtreeAdLinksQueue;
        this.otodomAdLinksQueue = otodomAdLinksQueue;
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
