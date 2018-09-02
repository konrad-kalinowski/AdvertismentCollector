package com.github.gumtree.crawler.adparsers.olx;

import com.github.gumtree.crawler.adparsers.AbstractAdInfoCollector;
import com.github.gumtree.crawler.adparsers.Domain;
import com.github.gumtree.crawler.adparsers.JsoupProvider;
import com.github.gumtree.crawler.adparsers.ValueParsers;
import com.github.gumtree.crawler.model.AdLink;
import com.github.gumtree.crawler.model.Advertisement;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.github.gumtree.crawler.model.Advertisement.VALUE_NOT_SET;
import static org.jsoup.nodes.Entities.EscapeMode.xhtml;

@Service
public class AdInfoCollectorOlx extends AbstractAdInfoCollector {

    @Autowired
    public AdInfoCollectorOlx(JsoupProvider jsoupProvider,
            @Qualifier("olxAdLinksQueue") LinkedBlockingQueue<AdLink> adLinkBlockingQueue,
            BlockingQueue<Advertisement> advertisementQueue,
            @Value("${adverts.collector.inactivity.seconds:5}") int inactivityPeriodSeconds) {
        super(jsoupProvider, adLinkBlockingQueue, advertisementQueue, inactivityPeriodSeconds);
    }

    @PostConstruct
    public void scheduleCollectingAdInfo() {
        super.scheduleCollectingAdInfo("olxCollector");
    }

    @Override
    public boolean canProcess(String advertLink) {
        return advertLink.startsWith(Domain.OLX_DOMAIN);
    }

    @Override
    public Advertisement collectAdInfo(String country, String city, Document document) {
        String title = document.title();
        document.outputSettings().escapeMode(xhtml);
        String priceText = document.select("div.price-label > strong").first().text();
        String text = priceText.replace(" zł", "").replaceAll("\\h+", "");
        double price = StringUtils.isNumeric(text) ? Double.parseDouble(text) : VALUE_NOT_SET;
        String areaText = document.select("th:contains(Powierzchnia) ~ td").text().replace(" m²", "");
        double area = StringUtils.isBlank(areaText) ? VALUE_NOT_SET : ValueParsers.parseValue(areaText);
        String descriptionText = document.select("#textContent").text();
        return new Advertisement.AdvertBuilder(title, document.location())
                .area(area)
                .description(descriptionText)
                .country(country)
                .city(city)
                .price(price)
                .build();
    }
}
