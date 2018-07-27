package com.github.gumtree.crawler.config;

import com.github.gumtree.crawler.StreetNamesProvider;
import com.github.gumtree.crawler.adparsers.DuplicatedLinkChecker;
import com.github.gumtree.crawler.db.AdCollectorDao;
import com.github.gumtree.crawler.model.StreetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class AppConfiguration {
    private static final Logger log = LoggerFactory.getLogger(AppConfiguration.class);

    @Bean
    public DuplicatedLinkChecker duplicatedLinkChecker(AdCollectorDao adCollectorDao) {
        List<String> storedLinks = adCollectorDao.getStoredLinks();
        return new DuplicatedLinkChecker(storedLinks);
    }

    @Bean
    public Map<StreetType, Set<String>> availableStreets(StreetNamesProvider streetNamesProvider) {
        String city = "Kraków";
        Set<Integer> citySymbolInSincDB = streetNamesProvider.findCitySymbolInSincDB(12, 61, city);
        log.debug("{} city symbol = {}", city, citySymbolInSincDB);
        Map<StreetType, Set<String>> streets = streetNamesProvider.findStreets(citySymbolInSincDB);
        log.debug("Initialized with {} streets {}", streets.size(), streets);
        return streets;
    }
}
