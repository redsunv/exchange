package org.example.dao;

import org.example.connection.DatabaseConfig;
import org.example.entiny.Currency;
import org.example.exception.DatabaseAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAOImpl implements CurrencyDAO {


    @Override
    public Optional<Currency> findByCode(String code) {
        String sql = "SELECT id, code, full_name, sign FROM currencies WHERE code = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, code.toUpperCase());

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
            throw new DatabaseAccessException("Ошибка при поиске валюты по коду: " + code, e);
        }

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
            throw new DatabaseAccessException("Ошибка получения валют", e);
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
            throw new DatabaseAccessException("Ошибка поиска по ID: " + id, e);
        }
    }

    @Override
    public Currency save(Currency currency) {
        String sql;
        boolean hasId = currency.getId() != null;

        if (hasId) {

            sql = "INSERT INTO currencies (id, code, full_name, sign) VALUES (?, ?, ?, ?)";
        } else {

            sql = "INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?)";
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (hasId) {
                stmt.setLong(1, currency.getId());
                stmt.setString(2, currency.getCode());
                stmt.setString(3, currency.getFull_name());
                stmt.setString(4, currency.getSign());
            } else {
                stmt.setString(1, currency.getCode());
                stmt.setString(2, currency.getFull_name());
                stmt.setString(3, currency.getSign());
            }

            stmt.executeUpdate();


            if (!hasId) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        currency.setId(rs.getLong(1));
                    }
                }
            }

            return currency;

        } catch (SQLException e) {
            throw new DatabaseAccessException("Ошибка при сохранении валюты: " + currency.getCode(), e);
        }
    }

    @Override
    public Optional<Currency> update(Currency currency) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть null");
        }
        String sql = "DELETE FROM currencies WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            int deletedRows = statement.executeUpdate();

            if (deletedRows == 0) {
                throw new DatabaseAccessException("Валюта с ID " + id + " не найдена");
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException("Ошибка при удалении валюты с ID: " + id, e);
        }
    }
}

