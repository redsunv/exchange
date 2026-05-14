package org.example.dao.exchange;

import org.example.model.ExchangeRate;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateDAO {

    List<ExchangeRate> findAllWithCurrencies();
    Optional<ExchangeRate> findById(Long id);
    ExchangeRate save(ExchangeRate exchangeRate);
    Optional<ExchangeRate> update(ExchangeRate exchangeRate);
    void delete(Long id);

}