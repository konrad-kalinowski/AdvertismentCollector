package com.github.gumtree.crawler.adparsers.olx;

import com.github.gumtree.crawler.adparsers.JsoupProvider;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AdListLinkCollectorOlx {
    private static final Logger log = LoggerFactory.getLogger(AdListLinkCollectorOlx.class);
    private final JsoupProvider jsoupProvider;

    public AdListLinkCollectorOlx(JsoupProvider jsoupProvider) {
        this.jsoupProvider = jsoupProvider;
    }


    public List<String> getAdvertLinks(String sectionLink, int depthLimit, int inactivePeroidOfSeconds) {
        Document document = jsoupProvider.connect(sectionLink);
        return getLinks(document, depthLimit, inactivePeroidOfSeconds);
    }

    public List<String> getAdvertsLinks(File file, int depthLimit, int inactivePeriodOfSeconds) {
        Document document = jsoupProvider.parseFile(file);
        return getLinks(document, depthLimit, inactivePeriodOfSeconds);
    }

    private List<String> getLinks(Document document, int depthLimit, int inactivePeriodOfSeconds) {
        List<String> allCollectedLinks = new ArrayList<>();
        for (int i = 0; i < depthLimit; i++) {
            Elements linkElements = document.select("td[class=offer] h3 a");
            List<String> advertLinks = linkElements.stream()
                    .map(path -> path.attr("href"))
                    .collect(Collectors.toList());
            log.debug("Collected {} advert links", advertLinks.size());
            allCollectedLinks.addAll(advertLinks);
            Elements nextPageLinkElement = document.select("div.pager span.next a");
            Element first = nextPageLinkElement.first();
            String nextPageLink = first.attr("href");
            try {
                Thread.sleep(inactivePeriodOfSeconds * 1000);
            } catch (InterruptedException e) {
                log.error("Interrupted");
            }
            log.debug("Fetching next page {}",nextPageLink);
            document = jsoupProvider.connect(nextPageLink);

        }
        return allCollectedLinks;
    }
}

