package org.example.dao.exchange;

import org.example.connection.DatabaseConfig;
import org.example.exception.DatabaseAccessException;
import org.example.model.ExchangeRate;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class JdbcExchangeRateDaoImpl implements ExchangeRateDAO {

    @Override
    public Optional<ExchangeRate> findByCode(String baseCurrencyCode, String targetCurrencyCode) {
        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> getAll() {
        return List.of();
    }

    @Override
    public Optional<ExchangeRate> findById(Long id) {
        final String query = """
                SELECT
                er.id AS id,
                bc.id AS base_id,
                bc.code AS base_code,
                bc.fullName AS base_name,
                bc.sign AS base_sign,
                tc.id AS target_id,
                tc.code AS target_code,
                tc.fullName AS target_name,
                tc.sign AS target_sign,
                er.rate AS rate
                FROM exchange_rates er
                INNER JOIN currencies bc ON er.base_currency_id = bc.id
                INNER JOIN currencies tc ON er.target_currency_id = tc.id WHERE er.id=?
                """;
        try {
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of();
            }


        } catch (SQLException e) {
            throw new DatabaseAccessException(" " + id);
        }
        return Optional.empty();
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        return null;
    }

    @Override
    public Optional<ExchangeRate> update(ExchangeRate exchangeRate) {
        return Optional.empty();
    }

    @Override
    public void delete(Long aLong) {

    }
}
