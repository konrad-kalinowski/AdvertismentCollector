package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.model.AdLink;
import com.github.gumtree.crawler.model.AdLinks;
import com.github.gumtree.crawler.nominatim.model.AdvertSearchSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Service
public class CompositeAdLinksCollector {
    private final List<AdLinksCollector> adLinksCollectors;
    private final BlockingQueue<AdLink> adLinksQueue;

    @Autowired
    public CompositeAdLinksCollector(List<AdLinksCollector> adLinksCollectors, LinkedBlockingQueue<AdLink> adLinksQueue) {
        this.adLinksCollectors = adLinksCollectors;
        this.adLinksQueue = adLinksQueue;
    }


    public void getAdvertLinks(AdvertSearchSpec advertSearchSpec, int depthLimit) {
        for (String link : advertSearchSpec.getLinks()) {
            AdLinks advertLinks = adLinksCollectors.stream()
                    .filter(collector -> collector.canProcess(link))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("Cant find links collector for link = " + link))
                    .getAdvertLinks(link,
                            advertSearchSpec.getCity(),
                            advertSearchSpec.getCountry(),
                            depthLimit);

            advertLinks.getLinks().stream()
                    .map(url -> new AdLink(url, advertLinks.getCountry(), advertLinks.getCity()))
                    .forEach(adLink -> adLinksQueue.offer(adLink));
        }
    }
}
