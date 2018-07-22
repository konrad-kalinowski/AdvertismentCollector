package com.github.gumtree.crawler.adparsers.olx;

import com.github.gumtree.crawler.adparsers.AdInfoCollector;
import com.github.gumtree.crawler.adparsers.JsoupProvider;
import com.github.gumtree.crawler.model.Advertisement;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

import java.io.File;

import static org.jsoup.nodes.Entities.EscapeMode.xhtml;

public class AdInfoCollectorOlx implements AdInfoCollector {
    private static JsoupProvider jsoupProvider;

    public AdInfoCollectorOlx(JsoupProvider jsoupProvider) {
        this.jsoupProvider=jsoupProvider;
    }

    public Advertisement collectInfo(File file){
        Document document = jsoupProvider.parseFile(file);
        return collectAdInfo(document);
    }

    @Override
    public Advertisement collectAdInfo(Document document) {
        String title = document.title();
        document.outputSettings().escapeMode(xhtml);
        String priceText = document.select("div.price-label > strong").first().text();
        String text = priceText.replace(" zł", "").replaceAll("\\h+", "");
        double price = StringUtils.isNumeric(text) ? Double.parseDouble(text) : Double.MIN_VALUE;
        String areaText = document.select("th:contains(Powierzchnia) ~ td").text().replace(" m²", "");
        double area = StringUtils.isBlank(areaText) ? Double.MIN_VALUE : Double.parseDouble(areaText);
        String descriptionText = document.select("#textContent").text();
        String location = document.select("div [class=offer-titlebox__details] > a").text();
        return new Advertisement.AdvertBuilder(title, document.location())
                .area(area)
                .description(descriptionText)
                .location(location)
                .price(price)
                .build();
    }

}