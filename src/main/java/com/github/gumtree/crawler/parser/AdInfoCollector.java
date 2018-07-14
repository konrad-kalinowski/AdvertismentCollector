package com.github.gumtree.crawler.parser;

import com.github.gumtree.crawler.model.Advertisement;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

import static org.jsoup.nodes.Entities.EscapeMode.xhtml;

public class AdInfoCollector {


    public Advertisement collectAdInfo(String adLink) {
        Document connect = JsoupProvider.connect(adLink);
        return getAdvertInfo(connect);
    }

    public Advertisement collectAdFromFile(File file) {
        Document document = JsoupProvider.parseFile(file);
        return getAdvertInfo(document);
    }


    private Advertisement getAdvertInfo(Document doc) {
        String title = doc.title();
        doc.outputSettings().escapeMode(xhtml);
        Element priceElement = doc.select("div[class=price] span[class=amount]").first();
        String text = priceElement.text().replace(" zł", "").replaceAll("\\h+", "");
        double price = StringUtils.isNumeric(text) ?  Double.parseDouble(text) : Double.MIN_VALUE;
        Elements areaElement = doc.select("div[class=attribute] span:contains(Wielkość (m2)) ~ span");
        double area = StringUtils.isBlank(areaElement.text()) ? Double.MIN_VALUE : Double.parseDouble(areaElement.text());
        Elements descriptionElement = doc.select("div[class=description] span[class=pre]");
        String description = descriptionElement.text();
        String location = doc.select("div[class=attribute] span:contains(Lokalizacja) ~ span").text();
        return new Advertisement.AdvertBuilder(title, doc.location())
                .area(area)
                .description(description)
                .location(location)
                .price(price)
                .build();


    }
}
