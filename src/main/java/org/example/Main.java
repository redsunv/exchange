package org.example;

import org.example.connection.DatabaseConfig;
import org.example.dao.currency.CurrencyDAOImpl;
import org.example.dao.exchange.ExchangeRateDAO;
import org.example.dao.exchange.JdbcExchangeRateDaoImpl;
import org.example.model.ExchangeRate;

import java.math.BigDecimal;
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

        ExchangeRate rate = new ExchangeRate();
        rate.setBaseCurrencyId(2L);
        rate.setTargetCurrencyId(2L);
        rate.setRate(new BigDecimal("0.95"));

        JdbcExchangeRateDaoImpl dao = new JdbcExchangeRateDaoImpl();
        ExchangeRate saved = dao.save(rate);
        System.out.println("Сохранен ID: " + saved.getId());
    }
}



