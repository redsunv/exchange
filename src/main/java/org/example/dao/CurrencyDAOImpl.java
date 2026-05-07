package org.example.dao;

import org.example.connection.DatabaseConfig;
import org.example.entiny.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAOImpl implements CurrencyDAO {


    @Override
    public Optional<Currency> findByCode(String code) {

        return Optional.empty();
    }

    @Override
    public List<Currency> getAllCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        String sql = "SELECT * FROM currencies";

        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Currency currency = new Currency();
                currency.setId(resultSet.getLong("id"));
                currency.setCode(resultSet.getString("code"));
                currency.setFull_name(resultSet.getString("full_name"));
                currency.setSign(resultSet.getString("sign"));
                currencies.add(currency);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения валют", e);
        }

        return currencies;
    }


    @Override
    public Optional<Currency> findById(Long id) {
        String sql = "SELECT * FROM currencies WHERE id = ?";


        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Currency currency = new Currency();
                    currency.setId(resultSet.getLong("id"));
                    currency.setCode(resultSet.getString("code"));
                    currency.setFull_name(resultSet.getString("full_name"));
                    currency.setSign(resultSet.getString("sign"));
                    return Optional.of(currency);
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска по ID: " + id, e);
        }
    }

    @Override
    public Currency save(Currency currency) {
        String sql = "INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?)";
        try {
            Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFull_name());
            statement.setString(3, currency.getSign());


            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) currency.setId(resultSet.getLong(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currency;
    }

    @Override
    public Optional<Currency> update(Currency currency) {
        return Optional.empty();
    }

    @Override
    public void delete(Long aLong) {

    }
}
