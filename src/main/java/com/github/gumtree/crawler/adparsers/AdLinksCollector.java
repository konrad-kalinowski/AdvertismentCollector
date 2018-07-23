package com.github.gumtree.crawler.adparsers;

import org.jsoup.nodes.Document;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AdLinksCollector {
    protected final JsoupProvider jsoupProvider;
    private final DuplicatedLinkChecker duplicatedLinkChecker;

    public AdLinksCollector(JsoupProvider jsoupProvider, DuplicatedLinkChecker duplicatedLinkChecker) {
        this.jsoupProvider = jsoupProvider;
        this.duplicatedLinkChecker = duplicatedLinkChecker;
    }

    public List<String> getAdvertLinks(String sectionLink, int depthLimit, int inactivePeroidOfSeconds) {
        Document document = jsoupProvider.connect(sectionLink);
        return getLinks(document, depthLimit, inactivePeroidOfSeconds);
    }

    public List<String> getAdvertsLinks(File file, int depthLimit, int inactivePeriodOfSeconds) {
        Document document = jsoupProvider.parseFile(file);
        return getLinks(document, depthLimit, inactivePeriodOfSeconds);
    }

    protected Set<String> getNonParsedLinks(List<String> advertLinks) {
        return advertLinks.stream()
                .filter(link -> !duplicatedLinkChecker.isAlreadyInDB(link))
                .collect(Collectors.toSet());
    }


    protected abstract List<String> getLinks(Document document, int depthLimit, int inactivePeriodOfSeconds);

    protected abstract boolean canProcess(String sectionLink);
}

