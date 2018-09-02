package com.github.gumtree.crawler.adparsers.gumtree;

import com.github.gumtree.crawler.adparsers.AbstractAdInfoCollector;
import com.github.gumtree.crawler.adparsers.Domain;
import com.github.gumtree.crawler.adparsers.JsoupProvider;
import com.github.gumtree.crawler.adparsers.ValueParsers;
import com.github.gumtree.crawler.model.AdLink;
import com.github.gumtree.crawler.model.Advertisement;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class AdInfoCollectorGumTree extends AbstractAdInfoCollector {

    private static final Logger log = LoggerFactory.getLogger(AdInfoCollectorGumTree.class);

    @Autowired
    public AdInfoCollectorGumTree(JsoupProvider jsoupProvider,
            @Qualifier("gumtreeAdLinksQueue") LinkedBlockingQueue<AdLink> gumtreeAdLinkQueue,
            BlockingQueue<Advertisement> advertisementQueue,
            @Value("${adverts.collector.inactivity.seconds:5}") int inactivityPeriodSeconds) {
        super(jsoupProvider, gumtreeAdLinkQueue, advertisementQueue, inactivityPeriodSeconds);
    }

    @PostConstruct
    public void scheduleCollectingAdInfo() {
        super.scheduleCollectingAdInfo("gumTreeCollector");
    }

    @Override
    public boolean canProcess(String advertLink) {
        return advertLink.startsWith(Domain.GUMTREE_DOMAIN);
    }

    @Override
    public Advertisement collectAdInfo(String country, String city, Document document) {
        String title = document.title();
        document.outputSettings().escapeMode(xhtml);
        Element priceElement = document.select("div[class=price]").first();
        String text = priceElement.text().replace(" zł", "").replaceAll("\\h+", "");
        double price = StringUtils.isNumeric(text) ? Double.parseDouble(text) : VALUE_NOT_SET;
        Elements areaElement = document.select("div[class=attribute] span:contains(Wielkość (m2)) ~ span");
        double area =
                StringUtils.isBlank(areaElement.text()) ? VALUE_NOT_SET : ValueParsers.parseValue(areaElement.text());
        Elements descriptionElement = document.select("div[class=description] span[class=pre]");
        String description = descriptionElement.text();
        return new Advertisement.AdvertBuilder(title, document.location())
                .area(area)
                .description(description)
                .country(country)
                .city(city)
                .price(price)
                .build();
    }
}
