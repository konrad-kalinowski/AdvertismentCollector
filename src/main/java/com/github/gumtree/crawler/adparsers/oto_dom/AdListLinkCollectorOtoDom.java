package com.github.gumtree.crawler.adparsers.oto_dom;

import com.github.gumtree.crawler.adparsers.AdLinksCollector;
import com.github.gumtree.crawler.adparsers.Domain;
import com.github.gumtree.crawler.adparsers.DuplicatedLinkChecker;
import com.github.gumtree.crawler.adparsers.JsoupProvider;
import com.google.common.util.concurrent.Uninterruptibles;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AdListLinkCollectorOtoDom extends AdLinksCollector {

    private static final Logger log = LoggerFactory.getLogger(AdListLinkCollectorOtoDom.class);

    public AdListLinkCollectorOtoDom(JsoupProvider jsoupProvider, DuplicatedLinkChecker duplicatedLinkChecker) {
        super(jsoupProvider, duplicatedLinkChecker);
    }

    @Override
    protected boolean canProcess(String sectionLink) {
        return sectionLink.startsWith(Domain.OTODOM_DOMAIN);
    }


    @Override
    protected List<String> getLinks(Document document, int depthLimit, int inactivePeriodOfSeconds) {
        List<String> allCollectedLinks = new ArrayList<>();
        for (int i = 0; i < depthLimit; i++) {
            Elements linkElements = document.select("div[class=offer-item-details] h3");
            List<String> advertLinks = linkElements.stream()
                    .map(path -> path.attr("href"))
                    .collect(Collectors.toList());
            log.debug("Collected {} adverts links", advertLinks.size());
            Set<String> uniqueLinks = getNonParsedLinks(advertLinks);
            allCollectedLinks.addAll(uniqueLinks);
            String nextPageLink = document.select("li[class=pager-next] a").first().attr("href");
            Uninterruptibles.sleepUninterruptibly(inactivePeriodOfSeconds, TimeUnit.SECONDS);
            log.debug("Fetching next page {}", nextPageLink);
            document = jsoupProvider.connect(nextPageLink);

        }
        return allCollectedLinks;
    }
}
