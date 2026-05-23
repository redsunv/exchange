package org.example.service;

import org.example.dao.currency.CurrencyDAO;
import org.example.dao.currency.CurrencyDAOImpl;
import org.example.dao.exchange.ExchangeRateDAO;
import org.example.dao.exchange.JdbcExchangeRateDaoImpl;
import org.example.dto.exchange.ExchangeRateResponseDTO;
import org.example.mapper.ExchangeRateMapper;
import org.example.model.Currency;
import org.example.model.ExchangeRate;
import org.example.validator.ExchangeRateValidator;

import java.math.BigDecimal;
import java.util.List;


public class ExchangeRateService {
    private final ExchangeRateDAO exchangeRateDAO = new JdbcExchangeRateDaoImpl();
    private final CurrencyDAO currencyDAO = new CurrencyDAOImpl();

    public List<ExchangeRateResponseDTO> getAllExchangeRates() {
        List<ExchangeRate> rates = exchangeRateDAO.getAll();
        return ExchangeRateMapper.toResponseDTOList(rates);
    }

    public ExchangeRateResponseDTO createNewExchangeRate(String baseCode, String targetCode, BigDecimal rate) {

        ExchangeRateValidator.validateCreateExchangeRate(baseCode, targetCode, rate, exchangeRateDAO, currencyDAO);

        Currency baseCurrency = currencyDAO.findByCode(baseCode).get();
        Currency targetCurrency = currencyDAO.findByCode(targetCode).get();

        ExchangeRate newExchangeRate = new ExchangeRate();
        newExchangeRate.setBaseCurrencyId(baseCurrency.getId());
        newExchangeRate.setTargetCurrencyId(targetCurrency.getId());
        newExchangeRate.setRate(rate);
        newExchangeRate.setBaseCurrency(baseCurrency);
        newExchangeRate.setTargetCurrency(targetCurrency);

        ExchangeRate saved = exchangeRateDAO.save(newExchangeRate);


        return ExchangeRateMapper.toResponseDTO(saved);
    }
}

