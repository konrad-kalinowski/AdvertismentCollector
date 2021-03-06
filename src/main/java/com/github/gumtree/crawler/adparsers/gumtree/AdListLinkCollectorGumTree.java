package com.github.gumtree.crawler.adparsers.gumtree;

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
public class AdListLinkCollectorGumTree extends AdLinksCollector {

    private static final Logger log = LoggerFactory.getLogger(AdListLinkCollectorGumTree.class);

    @Autowired
    public AdListLinkCollectorGumTree(JsoupProvider jsoupProvider,
                                      DuplicatedLinkChecker duplicatedLinkChecker,
                                      @Value("${ad.links.collector.inactive.period.seconds:5}") int inactivePeriodSeconds) {
        super(jsoupProvider, duplicatedLinkChecker, inactivePeriodSeconds);
    }


    @Override
    protected boolean canProcess(String sectionLink) {
        return sectionLink.startsWith(Domain.GUMTREE_DOMAIN);
    }

    @Override
    protected List<String> getLinks(Document doc, int depthLimit) {
        List<String> allCollectedLinks = new ArrayList<>();
        for (int i = 0; i < depthLimit; i++) {
            Elements linkElements = doc.select("div[class=title] a[href]");
            List<String> advertLinks = linkElements.stream()
                    .map(path -> path.attr("href"))
                    .map(path -> Domain.getFullLink(path))
                    .collect(Collectors.toList());
            log.debug("Collected {} advert links", advertLinks.size());
            Set<String> nonParsedLinks = getNonParsedLinks(advertLinks);
            allCollectedLinks.addAll(nonParsedLinks);
            Element nextPageLinkElement = doc.select("div[class=pagination]  a[class=next follows]").first();
            String nextPageLink = nextPageLinkElement.attr("href");
            nextPageLink = Domain.getFullLink(nextPageLink);
            sleepForInactivityPeriod();
            log.debug("Fetching next page {}", nextPageLink);
            doc = jsoupProvider.connect(nextPageLink);

        }
        return allCollectedLinks;
    }

}
