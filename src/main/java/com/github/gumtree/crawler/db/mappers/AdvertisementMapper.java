package com.github.gumtree.crawler.db.mappers;

import com.github.gumtree.crawler.model.Advertisement;
import com.github.gumtree.crawler.model.Coordinates;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AdvertisementMapper implements ResultSetMapper<Advertisement> {
    private static final Logger log = LoggerFactory.getLogger(AdvertisementMapper.class);


    @Override
    public List<Advertisement> map(ResultSet resultSet) {
        List<Advertisement> advertisements = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("ADVERTS.ID");
                String title = resultSet.getString("ADVERTS.TITLE");
                String link = resultSet.getString("ADVERTS.LINK");
                double price = resultSet.getDouble("ADVERTS.PRICE");
                String description = resultSet.getString("ADVERTS.DESCRIPTION");
                String country = resultSet.getString("ADVERTS.COUNTRY");
                String city = resultSet.getString("ADVERTS.CITY");
                String streets = resultSet.getString("ADVERTS.STREETS");
                double area = resultSet.getDouble("ADVERTS.AREA");
                Set<String> addressesSet = Sets.newHashSet(streets.split(","));
                double pricePerSquareMeter = resultSet.getDouble("ADVERTS.PRICEPERMETER");
                String coordinatesText = resultSet.getString("ADVERTS.COORDINATES");
                Coordinates coordinates = Coordinates.EMPTY_COORDINATES;
                if (!coordinatesText.equals("null,null")) {
                    String[] split = coordinatesText.split(",");
                    coordinates = new Coordinates(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
                }

                advertisements.add(new Advertisement.AdvertBuilder(title, link)
                        .id(id)
                        .price(price)
                        .description(description)
                        .country(country)
                        .city(city)
                        .streets(addressesSet)
                        .area(area)
                        .pricePerSquareMeter(pricePerSquareMeter)
                        .coordinates(coordinates)
                        .build());

            }
        } catch (SQLException e) {
            log.error("Failed while mapping to advertisement list", e);
        }
        return advertisements;

    }
}
