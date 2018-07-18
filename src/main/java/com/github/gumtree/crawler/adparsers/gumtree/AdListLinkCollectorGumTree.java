package com.github.gumtree.crawler.adparsers.gumtree;

import com.github.gumtree.crawler.adparsers.GumTreeLinkUtil;
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

public class AdListLinkCollectorGumTree {

    private static final Logger log = LoggerFactory.getLogger(AdListLinkCollectorGumTree.class);
    private final JsoupProvider jsoupProvider;

    public AdListLinkCollectorGumTree(JsoupProvider jsoupProvider) {
        this.jsoupProvider = jsoupProvider;
    }


    public List<String> getAdvertsLinks(String sectionLink, int depthLimit,int inactivePeriodOfSeconds) {
        Document document = jsoupProvider.connect(sectionLink);
        return getLinks(document, depthLimit,inactivePeriodOfSeconds);
    }

    public List<String> getAdvertsLinks(File file, int depthLimit,int inactivePeriodOfSeconds ) {
        Document document = jsoupProvider.parseFile(file);
        return getLinks(document, depthLimit,inactivePeriodOfSeconds);
    }


    private List<String> getLinks(Document doc, int depthLimit, int inactivePeriodOfSeconds) {
        List<String> allCollectedLinks = new ArrayList<>();
        for (int i = 0; i < depthLimit; i++) {
            Elements linkElements = doc.select("div[class=title] a[href]");
            List<String> advertLinks = linkElements.stream()
                    .map(path -> path.attr("href"))
                    .map(path -> GumTreeLinkUtil.getFullLink(path))
                    .collect(Collectors.toList());
            log.debug("Collected {} advert links", advertLinks.size());
            allCollectedLinks.addAll(advertLinks);
            Element nextPageLinkElement = doc.select("div[class=pagination]  a[class=next follows]").first();
            String nextPageLink = nextPageLinkElement.attr("href");
            nextPageLink = GumTreeLinkUtil.getFullLink(nextPageLink);
            try {
                Thread.sleep(inactivePeriodOfSeconds * 1000);
            } catch (InterruptedException e) {
                log.error("Interrupted");
            }
            log.debug("Fetching next page {}",nextPageLink);
            doc = jsoupProvider.connect(nextPageLink);

        }
        return allCollectedLinks;
    }
}
