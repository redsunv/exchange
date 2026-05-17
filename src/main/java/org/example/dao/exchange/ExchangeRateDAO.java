package org.example.dao.exchange;

import org.example.dao.currency.CrudDAO;
import org.example.model.ExchangeRate;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateDAO extends CrudDAO<ExchangeRate, Long> {
    Optional<ExchangeRate> findByCode(String baseCurrencyCode, String targetCurrencyCode);


}