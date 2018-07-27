package com.github.gumtree.crawler.adparsers.olx;

import com.github.gumtree.crawler.adparsers.AdLinksCollector;
import com.github.gumtree.crawler.adparsers.Domain;
import com.github.gumtree.crawler.adparsers.DuplicatedLinkChecker;
import com.github.gumtree.crawler.adparsers.JsoupProvider;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdListLinkCollectorOlx extends AdLinksCollector {
    private static final Logger log = LoggerFactory.getLogger(AdListLinkCollectorOlx.class);

    @Autowired
    public AdListLinkCollectorOlx(JsoupProvider jsoupProvider,
                                  DuplicatedLinkChecker duplicatedLinkChecker,
                                  @Value("${ad.links.collector.inactive.period.seconds:5}") int inactivePeriodSeconds) {
        super(jsoupProvider, duplicatedLinkChecker, inactivePeriodSeconds);
    }

    @Override
    protected boolean canProcess(String sectionLink) {
        return sectionLink.startsWith(Domain.OLX_DOMAIN);
    }

    @Override
    protected List<String> getLinks(Document document, int depthLimit) {
        List<String> allCollectedLinks = new ArrayList<>();
        for (int i = 0; i < depthLimit; i++) {
            Elements linkElements = document.select("td[class=offer] h3 a");
            List<String> advertLinks = linkElements.stream()
                    .map(path -> path.attr("href"))
                    .filter(link -> link.startsWith(Domain.OLX_DOMAIN))
                    .collect(Collectors.toList());
            log.debug("Collected {} advert links", advertLinks.size());
            Set<String> uniqueLinks = getNonParsedLinks(advertLinks);
            allCollectedLinks.addAll(uniqueLinks);
            Elements nextPageLinkElement = document.select("div.pager span.next a");
            Element first = nextPageLinkElement.first();
            String nextPageLink = first.attr("href");
            sleepForInactivityPeriod();
            log.debug("Fetching next page {}", nextPageLink);
            document = jsoupProvider.connect(nextPageLink);

        }
        return allCollectedLinks;
    }

}

