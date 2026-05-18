package org.example.connection;

import org.example.exception.DatabaseAccessException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void init() {
        createCurrenciesTable();
        createExchangeRatesTable();
        insertInitialData();
        insertExchangeRatesData();
    }

    private static void createCurrenciesTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS currencies (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        code VARCHAR NOT NULL UNIQUE,
                        fullName VARCHAR NOT NULL,
                        sign VARCHAR NOT NULL
                    )
                """;

        executeTable(sql, "Таблица создана");
    }

    private static void insertInitialData() {
        String sql = """
                    INSERT OR IGNORE INTO currencies (code, fullName, sign) VALUES
                        ('USD', 'US Dollar', '$'),
                        ('EUR', 'Euro', '€'),
                        ('RUB', 'Russian Ruble', '₽'),
                        ('GBP', 'British Pound', '£'),
                        ('JPY', 'Japanese Yen', '¥')
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            int inserted = stmt.executeUpdate(sql);
            System.out.println("Добавлено строк: " + inserted);


        } catch (SQLException e) {
            System.err.println("Ошибка вставки начальных данных: " + e.getMessage());
        }
    }

    private static void createExchangeRatesTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS exchange_rates (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        base_currency_id INTEGER NOT NULL,
                        target_currency_id INTEGER NOT NULL,
                        rate DECIMAL(10,6) NOT NULL,
                        FOREIGN KEY (base_currency_id) REFERENCES currencies(id),
                        FOREIGN KEY (target_currency_id) REFERENCES currencies(id),
                        UNIQUE(base_currency_id, target_currency_id)
                    )
                """;

        executeTable(sql, "Таблица exchange_rates создана");
    }
    private static void insertExchangeRatesData() {
        String sql = """
        INSERT OR IGNORE INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES
            (1, 2, 0.920000),
            (1, 3, 0.790000),
            (1, 4, 155.500000),
            (2, 1, 1.087000),
            (2, 3, 0.858700),
            (2, 4, 169.020000),
            (3, 1, 1.265800),
            (3, 2, 1.164500),
            (3, 4, 196.840000),
            (4, 1, 0.006430),
            (4, 2, 0.005920),
            (4, 3, 0.005080)
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            int inserted = stmt.executeUpdate(sql);
            System.out.println("Добавлено курсов валют: " + inserted);

        } catch (SQLException e) {
            System.err.println("Ошибка вставки курсов валют: " + e.getMessage());
        }
    }
    private static void executeTable(String sql, String s) {
        try {
            Connection connection = DatabaseConfig.getConnection();
            Statement statement = connection.createStatement();

            statement.execute(sql);

        } catch (SQLException e) {
            throw new DatabaseAccessException("Ошибка создания таблицы: " + e.getMessage(), e);
        }
    }
}

