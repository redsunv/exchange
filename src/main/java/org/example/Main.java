package org.example;

import org.example.connection.DatabaseConfig;
import org.example.dao.currency.CurrencyDAOImpl;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {


        try {
            Class.forName("org.sqlite.JDBC");
            Connection co = DriverManager.getConnection("jdbc:sqlite:C:Users/pronkin/mydatabase.db");
            System.out.println("Connection");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


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

    }


}