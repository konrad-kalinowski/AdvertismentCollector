package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.model.Advertisement;
import org.jsoup.nodes.Document;

import java.io.File;

public interface AdInfoCollector {
    Advertisement collectAdInfo(Document document);

    boolean canProcess(String advertLink);
}
