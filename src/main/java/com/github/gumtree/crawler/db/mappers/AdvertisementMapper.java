package com.github.gumtree.crawler.db.mappers;

import com.github.gumtree.crawler.model.Advertisement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdvertisementMapper implements ResultSetMapper<Advertisement> {
    private static final Logger log = LoggerFactory.getLogger(AdvertisementMapper.class);


    @Override
    public List<Advertisement> map(ResultSet resultSet) {
        List<Advertisement> advertisements = new ArrayList<>();

        try {
            while (resultSet.next()) {
                String title = resultSet.getString("ADVERTS.TITLE");
                String link = resultSet.getString("ADVERTS.LINK");
                double price = resultSet.getDouble("ADVERTS.PRICE");
                String description = resultSet.getString("ADVERTS.DESCRIPTION");
                String location = resultSet.getString("ADVERTS.LOCATION");
                double area = resultSet.getDouble("ADVERTS.AREA");
                advertisements.add(new Advertisement.AdvertBuilder(title,link)
                        .price(price)
                        .description(description)
                        .location(location)
                        .area(area)
                        .build());
            }
        } catch (SQLException e) {
            log.error("Failed while mapping to books list", e);
        }
        return advertisements;

    }
}
