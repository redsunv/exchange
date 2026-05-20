package org.example.dao.exchange;

import org.example.connection.DatabaseConfig;
import org.example.exception.DatabaseAccessException;
import org.example.exception.NotFoundException;
import org.example.mapper.ExchangeRateMapper;
import org.example.model.ExchangeRate;
import org.example.validator.ExchangeRateValidator;
import org.sqlite.SQLiteErrorCode;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcExchangeRateDaoImpl implements ExchangeRateDAO {

    @Override
    public Optional<ExchangeRate> findByCode(String baseCurrencyCode, String targetCurrencyCode) {
        final String sql = """
                SELECT
                er.id AS id,
                bc.id AS base_id,
                bc.code AS base_code,
                bc.fullName AS base_fullName,
                bc.sign AS base_sign,
                tc.id AS target_id,
                tc.code AS target_code,
                tc.fullName AS target_fullName,
                tc.sign AS target_sign,
                er.rate AS rate
                FROM exchange_rates er
                INNER JOIN currencies bc ON er.base_currency_id = bc.id
                INNER JOIN currencies tc ON er.target_currency_id = tc.id 
                WHERE bc.code=? AND tc.code=?
                """;

        try {
            Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);

            ResultSet resultSet =
                    statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ExchangeRateMapper.fromResultSetFull(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Ошибка поиска курса по кодам", e);
        }

        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> getAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        final String sql = """
                SELECT
                er.id AS id,
                bc.id AS base_id,
                bc.code AS base_code,
                bc.fullName AS base_fullName,
                bc.sign AS base_sign,
                tc.id AS target_id,
                tc.code AS target_code,
                tc.fullName AS target_fullName,
                tc.sign AS target_sign,
                er.rate AS rate
                FROM exchange_rates er
                INNER JOIN currencies bc ON er.base_currency_id = bc.id
                INNER JOIN currencies tc ON er.target_currency_id = tc.id
                ORDER BY er.id;
                """;

        try {
            Connection connection = DatabaseConfig.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                exchangeRates.add(ExchangeRateMapper.fromResultSetFull(resultSet));

            }
        } catch (SQLException e) {
            throw new NotFoundException("Ошибка получения курсов", e);
        }
        return exchangeRates;
    }

    @Override
    public Optional<ExchangeRate> findById(Long id) {
        final String query = """
                SELECT
                er.id AS id,
                bc.id AS base_id,
                bc.code AS base_code,
                bc.fullName AS base_fullName,
                bc.sign AS base_sign,
                tc.id AS target_id,
                tc.code AS target_code,
                tc.fullName AS target_fullName,
                tc.sign AS target_sign,
                er.rate AS rate
                FROM exchange_rates er
                INNER JOIN currencies bc ON er.base_currency_id = bc.id
                INNER JOIN currencies tc ON er.target_currency_id = tc.id WHERE er.id=?
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    ExchangeRate rate = ExchangeRateMapper.fromResultSetFull(rs);
                    return Optional.of(rate);
                } else {

                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException("Failed to find exchange rate by ID: " + id, e);
        }
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {

        ExchangeRateValidator exchangeRateValidator = new ExchangeRateValidator();
        exchangeRateValidator.validateDifferentPairs(exchangeRate.getBaseCurrencyId(), exchangeRate.getTargetCurrencyId());

        String sql = "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?) RETURNING id";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, exchangeRate.getBaseCurrencyId());
            statement.setLong(2, exchangeRate.getTargetCurrencyId());
            statement.setBigDecimal(3, exchangeRate.getRate());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Long Id = resultSet.getLong("id");
                    exchangeRate.setId(Id);
                    return exchangeRate;
                } else {
                    throw new DatabaseAccessException("Ошибка сохранения курса в базу данных. ID не получен");
                }
            }
        } catch (SQLException e) {

            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DatabaseAccessException("Курс для этой пары валют уже существует");
            }
            throw new DatabaseAccessException("Ошибка при сохранении курса: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<ExchangeRate> update(ExchangeRate exchangeRate) {
        return Optional.empty();
    }

    @Override
    public void delete(Long aLong) {

    }
}
