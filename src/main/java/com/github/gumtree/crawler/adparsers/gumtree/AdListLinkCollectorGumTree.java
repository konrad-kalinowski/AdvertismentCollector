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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AdListLinkCollectorGumTree extends AdLinksCollector {

    private static final Logger log = LoggerFactory.getLogger(AdListLinkCollectorGumTree.class);

    public AdListLinkCollectorGumTree(JsoupProvider jsoupProvider, DuplicatedLinkChecker duplicatedLinkChecker) {
        super(jsoupProvider, duplicatedLinkChecker);
    }

    @Override
    protected List<String> getLinks(Document doc, int depthLimit, int inactivePeriodOfSeconds) {
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
            try {
                Thread.sleep(inactivePeriodOfSeconds * 1000);
            } catch (InterruptedException e) {
                log.error("Interrupted");
            }
            log.debug("Fetching next page {}", nextPageLink);
            doc = jsoupProvider.connect(nextPageLink);

        }
        return allCollectedLinks;
    }
}
