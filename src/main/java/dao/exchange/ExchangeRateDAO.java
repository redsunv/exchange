package dao.exchange;

import dao.currency.CrudDAO;
import model.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateDAO extends CrudDAO<ExchangeRate, Long> {
    Optional<ExchangeRate> findByCode(String baseCurrencyCode, String targetCurrencyCode);


}