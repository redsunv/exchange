package org.example;

import org.example.connection.DatabaseConfig;
import org.example.dao.CurrencyDAOImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {

             Connection conn = DatabaseConfig.getConnection();
                 Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='currencies'");
        if (rs.next()){
            System.out.println("exist");
        }else
            System.out.println("not exist");
        rs.close();
        stmt.close();
        conn.close();

        CurrencyDAOImpl currencyDAO = new CurrencyDAOImpl();
        System.out.println(currencyDAO.getAllCurrencies());

        }
}