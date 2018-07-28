package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.model.Advertisement;
import org.jsoup.nodes.Document;

public interface AdInfoCollector {
    Advertisement collectAdInfo(String country, String city, Document document);

    boolean canProcess(String advertLink);
}
