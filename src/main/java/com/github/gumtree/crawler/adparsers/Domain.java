package com.github.gumtree.crawler.adparsers;

public class Domain {

    public static final String GUMTREE_DOMAIN = "https://www.gumtree.pl";
    public static final String OLX_DOMAIN = "https://www.olx.pl";
    public static final String OTODOM_DOMAIN = "https://www.otodom.pl";

    public static String getFullLink(String path){
        return GUMTREE_DOMAIN + path;
    }
}
