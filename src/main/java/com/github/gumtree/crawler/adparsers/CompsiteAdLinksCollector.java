package com.github.gumtree.crawler.adparsers;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Service
public class CompsiteAdLinksCollector extends AdLinksCollector {
    private final List<AdLinksCollector> adLinksCollectors;

    @Autowired
    public CompsiteAdLinksCollector(JsoupProvider jsoupProvider,
                                    DuplicatedLinkChecker duplicatedLinkChecker,
                                    List<AdLinksCollector> adLinksCollectors,
                                    @Value("${ad.links.collector.inactive.period.seconds:5}") int inactivePeriodSeconds) {
        super(jsoupProvider, duplicatedLinkChecker, inactivePeriodSeconds);
        this.adLinksCollectors = adLinksCollectors;
    }

    @Override
    public List<String> getAdvertLinks(String sectionLink, int depthLimit) {
        return adLinksCollectors.stream()
                .filter(collector -> collector.canProcess(sectionLink))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Cant find links collector for link = " + sectionLink))
                .getAdvertLinks(sectionLink, depthLimit);
    }

    @Override
    protected List<String> getLinks(Document document, int depthLimit) {
        return null;
    }

    @Override
    protected boolean canProcess(String sectionLink) {
        throw new NotImplementedException();
    }
}
