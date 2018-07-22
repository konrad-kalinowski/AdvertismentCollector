package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.model.Advertisement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DuplicatedLinkChecker implements OnBatchReadyListener {

    private static final Logger log = LoggerFactory.getLogger(DuplicatedLinkChecker.class);

    private final Set<String> uniqueLinks;

    public DuplicatedLinkChecker(Collection<String> uniqueLinks) {
        this.uniqueLinks = new HashSet<>(uniqueLinks);
        log.info("Initialize with {} unique links", uniqueLinks.size());
    }

    @Override
    public void onBatchReady(List<Advertisement> batch) {
        log.info("Adding new batch of {} elements to unique link list", batch.size());
        Set<String> newBatchLinks = batch.stream()
                .map(advertisement -> advertisement.getLink())
                .collect(Collectors.toSet());
        uniqueLinks.addAll(newBatchLinks);
    }

    public boolean isAlreadyInDB(String linkToCheck) {
        boolean alreadyExists = uniqueLinks.contains(linkToCheck);
        if (alreadyExists) {
            log.debug("Found duplicated link {}", linkToCheck);
        }
        return alreadyExists;
    }
}
