package org.example.dao.currency;

import org.example.connection.DatabaseConfig;
import org.example.exception.CurrencyAlreadyExistsException;
import org.example.exception.NotFoundException;
import org.example.model.Currency;
import org.example.exception.DatabaseAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAOImpl implements CurrencyDAO {


    @Override
    public Optional<Currency> findByCode(String code) {
        String sql = "SELECT id, code, fullName, sign FROM currencies WHERE code = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, code.toUpperCase());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Currency currency = new Currency();
                    currency.setId(resultSet.getLong("id"));
                    currency.setCode(resultSet.getString("code"));
                    currency.setFullName(resultSet.getString("fullName"));
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
                currency.setFullName(resultSet.getString("fullName"));
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
     final String sql = "SELECT * FROM currencies WHERE id = ?";


        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Currency currency = new Currency();
                    currency.setId(resultSet.getLong("id"));
                    currency.setCode(resultSet.getString("code"));
                    currency.setFullName(resultSet.getString("fullName"));
                    currency.setSign(resultSet.getString("sign"));
                    return Optional.of(currency);
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new NotFoundException("Ошибка поиска по ID: " + id, e);
        }
    }

    @Override
    public Currency save(Currency currency) {
        if (findByCode(currency.getCode()).isPresent()) {
            throw new CurrencyAlreadyExistsException("Валюта с кодом " + currency.getCode() + " уже существует");
        }
            String sql = "INSERT INTO currencies (code, fullName, sign) VALUES (?, ?, ?)";

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


                stmt.setString(1, currency.getCode());
                stmt.setString(2, currency.getFullName());
                stmt.setString(3, currency.getSign());


                stmt.executeUpdate();


                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        currency.setId(rs.getLong(1));
                    }
                }


                return currency;

            } catch (SQLException e) {
                throw new DatabaseAccessException("Ошибка при сохранении валюты: " + currency.getCode(), e);
            }
        }

    @Override
    public Optional<Currency> update(Currency currency) {
        String sql = "UPDATE currencies SET fullName = ?, sign = ? WHERE code = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, currency.getFullName());
            stmt.setString(2, currency.getSign());
            stmt.setString(3, currency.getCode());

            int updatedRows = stmt.executeUpdate();

            if (updatedRows > 0) {
                return Optional.of(currency);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseAccessException("Ошибка при обновлении валюты: " + currency.getCode(), e);
        }
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
                throw new NotFoundException("Валюта с ID " + id + " не найдена");
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException("Ошибка при удалении валюты с ID: " + id, e);
        }
    }
}

