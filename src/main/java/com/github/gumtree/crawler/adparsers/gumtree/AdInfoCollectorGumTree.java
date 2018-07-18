package com.github.gumtree.crawler.adparsers.gumtree;

import com.github.gumtree.crawler.adparsers.AdInfoCollector;
import com.github.gumtree.crawler.adparsers.JsoupProvider;
import com.github.gumtree.crawler.model.Advertisement;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

import static org.jsoup.nodes.Entities.EscapeMode.xhtml;

public class AdInfoCollectorGumTree implements AdInfoCollector {
    private final JsoupProvider jsoupProvider;

    public AdInfoCollectorGumTree(JsoupProvider jsoupProvider) {
        this.jsoupProvider = jsoupProvider;
    }

    public Advertisement collectInfo(File file){
        Document document = jsoupProvider.parseFile(file);
        return collectAdInfo(document);

    }

    @Override
    public Advertisement collectAdInfo(Document document) {
        String title = document.title();
        document.outputSettings().escapeMode(xhtml);
        Element priceElement = document.select("div[class=price]").first();
        String text = priceElement.text().replace(" zł", "").replaceAll("\\h+", "");
        double price = StringUtils.isNumeric(text) ? Double.parseDouble(text) : Double.MIN_VALUE;
        Elements areaElement = document.select("div[class=attribute] span:contains(Wielkość (m2)) ~ span");
        double area = StringUtils.isBlank(areaElement.text()) ? Double.MIN_VALUE : Double.parseDouble(areaElement.text());
        Elements descriptionElement = document.select("div[class=description] span[class=pre]");
        String description = descriptionElement.text();
        String location = document.select("div[class=attribute] span:contains(Lokalizacja) ~ span").text();
        return new Advertisement.AdvertBuilder(title, document.location())
                .area(area)
                .description(description)
                .location(location)
                .price(price)
                .build();
    }

}
