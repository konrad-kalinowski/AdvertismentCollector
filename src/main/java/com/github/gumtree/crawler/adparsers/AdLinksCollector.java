package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.model.AdLinks;
import com.google.common.util.concurrent.Uninterruptibles;
import org.jsoup.nodes.Document;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class AdLinksCollector {
    protected final JsoupProvider jsoupProvider;
    private final DuplicatedLinkChecker duplicatedLinkChecker;
    protected int inactivePeriodSeconds;


    public AdLinksCollector(JsoupProvider jsoupProvider, DuplicatedLinkChecker duplicatedLinkChecker, int inactivePeriodSeconds) {
        this.jsoupProvider = jsoupProvider;
        this.duplicatedLinkChecker = duplicatedLinkChecker;

        this.inactivePeriodSeconds = inactivePeriodSeconds;
    }

    public AdLinks getAdvertLinks(String link, String city, String country, int depthLimit) {
        Document document = jsoupProvider.connect(link);
        List<String> links = getLinks(document, depthLimit);
        return new AdLinks(country, city, links);
    }

    public List<String> getAdvertsLinks(File file, int depthLimit) {
        Document document = jsoupProvider.parseFile(file);
        return getLinks(document, depthLimit);
    }

    protected Set<String> getNonParsedLinks(List<String> advertLinks) {
        return advertLinks.stream()
                .filter(link -> !duplicatedLinkChecker.isAlreadyInDB(link))
                .collect(Collectors.toSet());
    }

    protected void sleepForInactivityPeriod() {

        Uninterruptibles.sleepUninterruptibly(inactivePeriodSeconds, TimeUnit.SECONDS);
    }


    protected abstract List<String> getLinks(Document document, int depthLimit);

    protected abstract boolean canProcess(String sectionLink);
}

