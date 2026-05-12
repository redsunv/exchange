package org.example.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    private static final HikariDataSource DATA_SOURCE;
    private static final String DB_URL = "jdbc:sqlite:C:/Users/pronkin/SQLite/exchange.db";
    private static final String DRIVER = "org.sqlite.JDBC";

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setDriverClassName(DRIVER);
        DATA_SOURCE = new HikariDataSource(config);

        createTableIfNotExist();
    }

    private static void createTableIfNotExist() {

        String createTableSql = """
            CREATE TABLE IF NOT EXISTS currencies (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                code VARCHAR NOT NULL UNIQUE,
                fullName VARCHAR NOT NULL,
                sign VARCHAR NOT NULL
            )
        """;


        String insertDataSql = """
            INSERT OR IGNORE INTO currencies (code, fullName, sign) VALUES
                ('USD', 'US Dollar', '$'),
                ('EUR', 'Euro', '€'),
                ('RUB', 'Russian Ruble', '₽'),
                ('GBP', 'British Pound', '£'),
                ('JPY', 'Japanese Yen', '¥')
        """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {


            statement.execute(createTableSql);
            System.out.println("Таблица создана");


            int inserted = statement.executeUpdate(insertDataSql);
            System.out.println("Добавлено строк: " + inserted);

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка инициализации БД", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}