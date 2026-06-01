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
                -- USD курсы
                (1, 2, 0.920000),   -- USD to EUR
                (1, 3, 90.500000),  -- USD to RUB
                (1, 4, 0.790000),   -- USD to GBP
                (1, 5, 155.500000), -- USD to JPY
                
                -- EUR курсы
                (2, 1, 1.087000),   -- EUR to USD
                (2, 3, 100.000000), -- EUR to RUB
                (2, 4, 0.858700),   -- EUR to GBP
                (2, 5, 169.020000), -- EUR to JPY
                
                -- RUB курсы
                (3, 1, 0.011050),   -- RUB to USD
                (3, 2, 0.010000),   -- RUB to EUR
                (3, 4, 0.008587),   -- RUB to GBP
                (3, 5, 1.720000),   -- RUB to JPY
                
                -- GBP курсы
                (4, 1, 1.265800),   -- GBP to USD
                (4, 2, 1.164500),   -- GBP to EUR
                (4, 3, 116.450000), -- GBP to RUB
                (4, 5, 196.840000), -- GBP to JPY
                
                -- JPY курсы
                (5, 1, 0.006430),   -- JPY to USD
                (5, 2, 0.005920),   -- JPY to EUR
                (5, 3, 0.581400),   -- JPY to RUB
                (5, 4, 0.005080)    -- JPY to GBP""";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            int inserted = stmt.executeUpdate(sql);
            System.out.println("Добавлено курсов валют: " + inserted);

        } catch (SQLException e) {
            System.err.println("Ошибка вставки курсов валют: " + e.getMessage());
        }
    }

    private static void executeTable(String sql, String successMessage) {
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println(successMessage);
        } catch (SQLException e) {
            throw new DatabaseAccessException("Ошибка создания таблицы: " + e.getMessage(), e);
        }
    }
}

