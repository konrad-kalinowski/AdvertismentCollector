package com.github.gumtree.crawler.adparsers.oto_dom;

import com.github.gumtree.crawler.adparsers.AdInfoCollector;
import com.github.gumtree.crawler.adparsers.Domain;
import com.github.gumtree.crawler.adparsers.JsoupProvider;
import com.github.gumtree.crawler.model.Advertisement;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

import static org.jsoup.nodes.Entities.EscapeMode.xhtml;

@Service
public class AdInfoCollectorOtoDom implements AdInfoCollector {

    private static JsoupProvider jsoupProvider;

    @Autowired
    public AdInfoCollectorOtoDom(JsoupProvider jsoupProvider) {
        this.jsoupProvider = jsoupProvider;
    }


    public Advertisement collectInfo(File file) {
        Document document = jsoupProvider.parseFile(file);
        return collectAdInfo(document);
    }

    @Override
    public boolean canProcess(String advertLink) {
        return advertLink.startsWith(Domain.OTODOM_DOMAIN);
    }

    @Override
    public Advertisement collectAdInfo(Document document) {
        String title = document.select("div[class=article-offer] header h1").text();
        document.outputSettings().escapeMode(xhtml);
        String priceText = document.select("strong[class=box-price-value] ").first().text();
        String text = priceText.replace("zł", "").replaceAll("\\h+", "");
        double price = StringUtils.isNumeric(text) ? Double.parseDouble(text) : Double.MIN_VALUE;
        String areaText = document.select("div[class=area-lane] span[class=big]").text().replace(" m²", "");
        double area = StringUtils.isNumeric(areaText) ? Double.parseDouble(areaText) : Double.MIN_VALUE;
        String description = document.select("div[itemprop=description]").text();
        String location = document.select("p[class=address-text]").first().text();
        double priceForSquare = area/price;
        return Advertisement.builder(title, document.location())
                .area(area)
                .location(location)
                .description(description)
                .price(price)
                .pricePerSquareMeter(priceForSquare)
                .build();
    }
}
