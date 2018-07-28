package com.github.gumtree.crawler.adparsers.gumtree;

import com.github.gumtree.crawler.adparsers.AdInfoCollector;
import com.github.gumtree.crawler.adparsers.Domain;
import com.github.gumtree.crawler.adparsers.JsoupProvider;
import com.github.gumtree.crawler.adparsers.ValueParsers;
import com.github.gumtree.crawler.model.Advertisement;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.github.gumtree.crawler.model.Advertisement.VALUE_NOT_SET;
import static org.jsoup.nodes.Entities.EscapeMode.xhtml;

@Service
public class AdInfoCollectorGumTree implements AdInfoCollector {
    private final JsoupProvider jsoupProvider;

    @Autowired
    public AdInfoCollectorGumTree(JsoupProvider jsoupProvider) {
        this.jsoupProvider = jsoupProvider;
    }

    @Override
    public boolean canProcess(String advertLink) {
        return advertLink.startsWith(Domain.GUMTREE_DOMAIN);
    }


    public Advertisement collectInfo(File file) {
        Document document = jsoupProvider.parseFile(file);
        return collectAdInfo(document);

    }

    @Override
    public Advertisement collectAdInfo(Document document) {
        String title = document.title();
        document.outputSettings().escapeMode(xhtml);
        Element priceElement = document.select("div[class=price]").first();
        String text = priceElement.text().replace(" zł", "").replaceAll("\\h+", "");
        double price = StringUtils.isNumeric(text) ? Double.parseDouble(text) : VALUE_NOT_SET;
        Elements areaElement = document.select("div[class=attribute] span:contains(Wielkość (m2)) ~ span");
        double area = StringUtils.isBlank(areaElement.text()) ? VALUE_NOT_SET : ValueParsers.parseValue(areaElement.text());
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
