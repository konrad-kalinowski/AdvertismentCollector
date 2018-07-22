package com.github.gumtree.crawler.db.mappers;

import com.github.gumtree.crawler.model.Advertisement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LinksMapper implements ResultSetMapper<String> {
    private static final Logger log = LoggerFactory.getLogger(LinksMapper.class);

    @Override
    public List<String> map(ResultSet resultSet) {
        List<String> links = new ArrayList<>();

        try {
            while (resultSet.next()) {
                String link = resultSet.getString("ADVERTS.LINK");
                links.add(link);
            }
        } catch (SQLException e) {
            log.error("Failed while mapping to links list", e);
        }
        return links;

    }
}
