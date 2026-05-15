package org.example.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class DatabaseConfig {
    private static final HikariDataSource DATA_SOURCE;
    private static final String DB_URL = "jdbc:sqlite:C:/Users/pronkin/SQLite/exchange.db";
    private static final String DRIVER = "org.sqlite.JDBC";

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setDriverClassName(DRIVER);
        DATA_SOURCE = new HikariDataSource(config);

        DatabaseInitializer.init();
    }


    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}