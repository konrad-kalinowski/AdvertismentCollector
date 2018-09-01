package com.github.gumtree.crawler.adparsers.oto_dom;

import com.github.gumtree.crawler.adparsers.AbstractAdInfoCollector;
import com.github.gumtree.crawler.adparsers.Domain;
import com.github.gumtree.crawler.adparsers.JsoupProvider;
import com.github.gumtree.crawler.adparsers.ValueParsers;
import com.github.gumtree.crawler.model.AdLink;
import com.github.gumtree.crawler.model.Advertisement;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;

import static com.github.gumtree.crawler.model.Advertisement.VALUE_NOT_SET;
import static org.jsoup.nodes.Entities.EscapeMode.xhtml;

@Service
public class AdInfoCollectorOtoDom extends AbstractAdInfoCollector {
    private static final Logger log = LoggerFactory.getLogger(AdInfoCollectorOtoDom.class);

    @Autowired
    public AdInfoCollectorOtoDom(JsoupProvider jsoupProvider,
                                 @Qualifier("otodomAdLinksQueue") BlockingQueue<AdLink> otodomAdLinksQueue,
                                 BlockingQueue<Advertisement> advertisementQueue) {
        super(jsoupProvider, otodomAdLinksQueue, advertisementQueue);
    }


    @PostConstruct
    public void scheduleCollectingAdInfo() {
        super.scheduleCollectingAdInfo("otoDomCollector");

    }

    @Override
    public boolean canProcess(String advertLink) {
        return advertLink.startsWith(Domain.OTODOM_DOMAIN);
    }

    @Override
    public Advertisement collectAdInfo(String country, String city, Document document) {
        String title = document.select("div[class=article-offer] header h1").text();
        document.outputSettings().escapeMode(xhtml);
        String priceText = document.select("li[class=param_price] span strong").text();
        String text = priceText.replace("zł", "").replaceAll("\\h+", "");
        double price = StringUtils.isNumeric(text) ? Double.parseDouble(text) : VALUE_NOT_SET;
        String areaText = document.select("div[class=area-lane] span[class=big]").text().replace(" m²", "");
        double area = ValueParsers.parseValue(areaText);
        String description = document.select("div[itemprop=description]").text();
        return Advertisement.builder(title, document.location())
                .area(area)
                .country(country)
                .city(city)
                .description(description)
                .price(price)
                .build();
    }
}
