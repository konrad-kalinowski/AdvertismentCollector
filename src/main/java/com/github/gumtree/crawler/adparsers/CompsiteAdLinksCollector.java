package com.github.gumtree.crawler.adparsers;

import org.jsoup.nodes.Document;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class CompsiteAdLinksCollector extends AdLinksCollector {
    private final List<AdLinksCollector> adLinksCollectors;

    public CompsiteAdLinksCollector(JsoupProvider jsoupProvider,
                                    DuplicatedLinkChecker duplicatedLinkChecker,
                                    List<AdLinksCollector> adLinksCollectors) {
        super(jsoupProvider, duplicatedLinkChecker);
        this.adLinksCollectors = adLinksCollectors;
    }

    @Override
    public List<String> getAdvertLinks(String sectionLink, int depthLimit, int inactivePeroidOfSeconds) {
        return adLinksCollectors.stream()
                .filter(collector -> collector.canProcess(sectionLink))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Cant find links collector for link = " + sectionLink))
                .getAdvertLinks(sectionLink, depthLimit, inactivePeroidOfSeconds);
    }

    @Override
    protected List<String> getLinks(Document document, int depthLimit, int inactivePeriodOfSeconds) {
        return null;
    }

    @Override
    protected boolean canProcess(String sectionLink) {
        throw new NotImplementedException();
    }
}
