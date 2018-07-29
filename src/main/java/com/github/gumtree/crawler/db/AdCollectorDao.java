package com.github.gumtree.crawler.db;

import com.github.gumtree.crawler.db.mappers.AdvertisementMapper;
import com.github.gumtree.crawler.db.mappers.LinksMapper;
import com.github.gumtree.crawler.db.mappers.ResultSetMapper;
import com.github.gumtree.crawler.model.Advertisement;
import com.github.gumtree.crawler.model.Coordinates;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AdCollectorDao {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AdCollectorDao.class);
    private static final String DUMP_FILE = "dump.sql";

    private Connection con;

    @Autowired
    public AdCollectorDao(Connection con) {
        this.con = con;
    }

    @PostConstruct
    public void initialize() {
        initialize("/skrypt.sql");
    }

    public void initialize(String fileName) {
        try {
            importSql(fileName);

        } catch (Exception e) {
            log.error("Failed to initialize server", e);
        }
    }

    private void importSql(String fileName) throws IOException, SQLException {
        InputStream sqlFile = AdCollectorDao.class.getResourceAsStream(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sqlFile));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        String[] queries = stringBuilder.toString().split(";");
        for (String query : queries) {
            if (StringUtils.isNotBlank(query)) {
                Statement st = con.createStatement();
                st.executeUpdate(query);
                st.close();
            }
        }
    }

    @PreDestroy
    public void close() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            log.error("Cannot close connection. ", e);
        }

    }

    public void addAdvert(Advertisement advertisement) {
        String query = "INSERT INTO ADVERTS(TITLE, LINK, PRICE, DESCRIPTION, COUNTRY, CITY, STREETS, AREA, PRICEPERMETER, COORDINATES)" +
                " VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, advertisement.getTitle());
            statement.setString(2, advertisement.getLink());
            statement.setDouble(3, advertisement.getPrice());
            statement.setString(4, advertisement.getDescription());
            statement.setString(5, advertisement.getCountry());
            statement.setString(6, advertisement.getCity());
            statement.setString(7, advertisement.getStreets().stream().collect(Collectors.joining(",")));
            statement.setDouble(8, advertisement.getArea());
            statement.setDouble(9, advertisement.getPricePerSquareMeter());
            Coordinates coordinates = advertisement.getCoordinates();
            String serializedCoordinates = "";
            if (coordinates != null) {

                serializedCoordinates = coordinates.getLongitude() + "," + advertisement.getCoordinates().getLatitude();
            }
            statement.setString(10, serializedCoordinates);
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Failed to add advert.", e);
        }

    }

    public void addAdverts(List<Advertisement> advertisements) {
        for (Advertisement advertisement : advertisements) {
            this.addAdvert(advertisement);
        }
    }

    public List<Advertisement> showAdverts(int startId, int limit) {
        String template = "SELECT * FROM ADVERTS WHERE ID > ? limit ?";
        try (PreparedStatement statement = con.prepareStatement(template)) {
            statement.setInt(1, startId);
            statement.setInt(2, limit);
            ResultSet resultSet = statement.executeQuery();
            return new AdvertisementMapper().map(resultSet);
        } catch (SQLException e) {
            throw new IllegalStateException("Query execution failed", e);
        }
    }

    private <T> List<T> executeQuery(String query, ResultSetMapper<T> mapper) {
        try (Statement statement = con.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            return mapper.map(resultSet);
        } catch (SQLException e) {
            throw new IllegalStateException("Query " + query + "execution failed", e);
        }
    }

    public List<String> getStoredLinks() {
        return executeQuery("SELECT LINK FROM ADVERTS", new LinksMapper());
    }


}

