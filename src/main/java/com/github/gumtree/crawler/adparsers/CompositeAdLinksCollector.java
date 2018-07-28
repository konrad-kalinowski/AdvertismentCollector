package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.model.AdLinks;
import com.github.gumtree.crawler.nominatim.model.AdvertSearchSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompositeAdLinksCollector {
    private final List<AdLinksCollector> adLinksCollectors;

    @Autowired
    public CompositeAdLinksCollector(
            List<AdLinksCollector> adLinksCollectors) {
        this.adLinksCollectors = adLinksCollectors;
    }

    public AdLinks getAdvertLinks(AdvertSearchSpec advertSearchSpec, int depthLimit) {
        List<String> gatheredLinks = new ArrayList<>();
        for (String link : advertSearchSpec.getLinks()) {
            AdLinks advertLinks = adLinksCollectors.stream()
                    .filter(collector -> collector.canProcess(link))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("Cant find links collector for link = " + link))
                    .getAdvertLinks(link,
                            advertSearchSpec.getCity(),
                            advertSearchSpec.getCountry(),
                            depthLimit);
            gatheredLinks.addAll(advertLinks.getLinks());
        }
        return new AdLinks(advertSearchSpec.getCountry(),advertSearchSpec.getCity(), gatheredLinks);
    }
}
