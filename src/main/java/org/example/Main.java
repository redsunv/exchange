package org.example;

import org.example.connection.DatabaseConfig;
import org.example.dao.currency.CurrencyDAOImpl;
import org.example.dao.exchange.ExchangeRateDAO;
import org.example.dao.exchange.JdbcExchangeRateDaoImpl;
import org.example.model.ExchangeRate;

import java.sql.*;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws SQLException {

        Connection conn = DatabaseConfig.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='currencies'");
        if (rs.next()) {
            System.out.println("exist");
        } else
            System.out.println("not exist");
        rs.close();
        stmt.close();
        conn.close();

        CurrencyDAOImpl currencyDAO = new CurrencyDAOImpl();
        System.out.println(currencyDAO.getAll());


        // Создаём экземпляр DAO
        ExchangeRateDAO exchangeRateDAO = new JdbcExchangeRateDaoImpl();

        // ТЕСТ 1: Поиск курса с ID = 1
        System.out.println("1. Поиск курса с ID = 1:");
        try {
            Optional<ExchangeRate> result = exchangeRateDAO.findById(1L);

            if (result.isPresent()) {
                ExchangeRate rate = result.get();
                System.out.println("   ✅ Курс найден!");
                System.out.println("   ID: " + rate.getId());
                System.out.println("   Rate: " + rate.getRate());
                System.out.println("   Base Currency ID: " + rate.getBaseCurrencyId());
                System.out.println("   Target Currency ID: " + rate.getTargetCurrencyId());

                // Если есть полные объекты валют
                if (rate.getBaseCurrency() != null && rate.getTargetCurrency() != null) {
                    System.out.println("   Базовая валюта: " + rate.getBaseCurrency().getCode() +
                            " - " + rate.getBaseCurrency().getFullName());
                    System.out.println("   Целевая валюта: " + rate.getTargetCurrency().getCode() +
                            " - " + rate.getTargetCurrency().getFullName());
                }
            } else {
                System.out.println("   ❌ Курс с ID=1 не найден");
            }
        } catch (Exception e) {
            System.out.println("   ❌ Ошибка: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();

        // ТЕСТ 2: Поиск несуществующего курса
        System.out.println("2. Поиск курса с ID = 999:");
        try {
            Optional<ExchangeRate> result = exchangeRateDAO.findById(999L);

            if (result.isEmpty()) {
                System.out.println("   ✅ Пустой Optional (курс не найден) - правильно!");
            } else {
                System.out.println("   ❌ Ошибка: курс найден, хотя не должен");
            }
        } catch (Exception e) {
            System.out.println("   ❌ Ошибка: " + e.getMessage());
        }

        System.out.println("\n=== ТЕСТ ЗАВЕРШЕН ===");
    }
}



