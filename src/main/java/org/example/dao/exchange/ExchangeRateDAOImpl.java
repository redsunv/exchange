package org.example.dao.exchange;

import org.example.connection.DatabaseConfig;
import org.example.model.ExchangeRate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDAOImpl implements ExchangeRateDAO {
    @Override
    public List<ExchangeRate> findAllWithCurrencies() {
        List<ExchangeRate> exchangeRate = new ArrayList<>();
        String sql = "SELECT * FROM exchangeRate";

        try {
            Connection connection= DatabaseConfig.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } {
        }

        return List.of();
    }

    @Override
    public Optional<ExchangeRate> findById(Long id) {
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
    public void delete(Long id) {

    }
}
