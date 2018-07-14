package com.github.gumtree.crawler.parser;

public class GumTreeLinkUtil {

    private static final String DOMAIN = "https://www.gumtree.pl";

    public static String getFullLink(String path){
        return DOMAIN + path;
    }
}
