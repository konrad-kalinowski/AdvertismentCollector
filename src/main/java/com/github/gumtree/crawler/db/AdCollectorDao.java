package com.github.gumtree.crawler.db;

import com.github.gumtree.crawler.model.Advertisement;
import com.github.gumtree.crawler.db.mappers.AdvertisementMapper;
import com.github.gumtree.crawler.db.mappers.ResultSetMapper;
import org.slf4j.LoggerFactory;
import org.h2.tools.Server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class AdCollectorDao {
    private Server tcpServer;
    private Server webServer;
    private String url;
    private Connection con;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AdCollectorDao.class);

    public void initialize() {
        String paramsString = "-baseDir /tmp/h2-test -tcpPort 8081 -tcpAllowOthers";
        String[] dbParams = paramsString.split(" ");
        try {
            tcpServer = Server.createTcpServer(dbParams).start();
            webServer = Server.createWebServer("-webAllowOthers", "-webPort", "8082").start();
            url = String.format("jdbc:h2:%s/test", tcpServer.getURL());
            System.out.println(tcpServer.getStatus());
            System.out.println(webServer.getStatus());
            con = DriverManager.getConnection(url, "sa", "");
            InputStream sqlFile = AdCollectorDao.class.getResourceAsStream("/skrypt.sql");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sqlFile));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            String[] queries = stringBuilder.toString().split(";");
            for (String query : queries) {
                Statement st = con.createStatement();
                st.executeUpdate(query);
                st.close();
            }

        } catch (Exception e) {
            log.error("Failed to initialize server", e);
        }

    }

    public void close() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            log.error("Cannot close connection. ", e);
        }
        if (tcpServer != null) {
            tcpServer.stop();
        }
        if (webServer != null) {
            webServer.stop();
        }

    }

    public void addAdvert(Advertisement advertisement) {
        String query = "INSERT INTO ADVERTS VALUES(?,?,?,?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, advertisement.getTitle());
            statement.setString(2, advertisement.getLink());
            statement.setDouble(3, advertisement.getPrice());
            statement.setString(4, advertisement.getDescription());
            statement.setString(5, advertisement.getLocation());
            statement.setDouble(6, advertisement.getArea());
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

    public List<Advertisement> showAdverts() {
        return executeQuery(String.format("SELECT * FROM ADVERTS"), new AdvertisementMapper());
    }


    private <T> List<T> executeQuery(String query, ResultSetMapper<T> mapper) {
        try (Statement statement = con.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            return mapper.map(resultSet);
        } catch (SQLException e) {
            throw new IllegalStateException("Query " + query + "execution failed", e);
        }
    }


}
