package org.example.dao.exchange;

import org.example.model.ExchangeRate;

import java.util.List;
import java.util.Optional;

public class ExchangeRateDAOImpl implements ExchangeRateDAO {
    @Override
    public List<ExchangeRate> findAll() {
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
