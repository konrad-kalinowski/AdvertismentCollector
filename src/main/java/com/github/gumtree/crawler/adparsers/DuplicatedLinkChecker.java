package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.model.Advertisement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
        try {
            String decodedLink = URLDecoder.decode(linkToCheck, StandardCharsets.UTF_8.toString());
            boolean alreadyExists = uniqueLinks.contains(decodedLink);
            if (alreadyExists) {
                log.debug("Found duplicated link {}", decodedLink);
            }
            return alreadyExists;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Failed to decode link " + linkToCheck, e);
        }
    }
}
