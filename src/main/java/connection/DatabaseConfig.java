package connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final HikariDataSource DATA_SOURCE;
    private static final String DB_URL = "jdbc:sqlite:/var/lib/tomcat10/webapps/exchange/data/exchange.db";
    private static final String DRIVER = "org.sqlite.JDBC";


    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(DB_URL);
            config.setDriverClassName(DRIVER);
            DATA_SOURCE = new HikariDataSource(config);


            DatabaseInitializer.init();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to initialize DB: " + e);
        }


    }


    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}