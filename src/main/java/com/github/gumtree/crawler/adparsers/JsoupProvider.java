package com.github.gumtree.crawler.adparsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsoupProvider {

    public Document connect(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.62 Safari/537.36")
                    .referrer("http://www.google.com")
                    .timeout(10 * 1000)
                    .get();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to connect to url . " + url, e);
        }
    }

    public Document parseFile(File file){
        try {
            return Jsoup.parse(file, StandardCharsets.UTF_8.toString());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse site from file.", e);
        }
    }
}
