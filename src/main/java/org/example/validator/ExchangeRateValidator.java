package org.example.validator;


import org.example.dao.currency.CurrencyDAO;
import org.example.dao.exchange.ExchangeRateDAO;
import org.example.exception.DatabaseAccessException;
import org.example.exception.ValidationException;

import java.math.BigDecimal;

public class ExchangeRateValidator {

    public void validateDifferentPairs(Long baseCurrencyId, Long targetCurrencyId) {
        if (baseCurrencyId.equals(targetCurrencyId)) {
            throw new DatabaseAccessException("The base and target currencies must be different");
        }

    }

    public void validateCreateExchangeRate(String baseCode, String targetCode, BigDecimal rate,
                                           ExchangeRateDAO exchangeRateDAO,
                                           CurrencyDAO currencyDAO) {
        if (baseCode.equals(targetCode)) {
            throw new ValidationException("Валюты должны быть разными");
        }
        if (exchangeRateDAO.findByCode(baseCode, targetCode).isPresent()) {
            throw new ValidationException("Такой курс уже есть");
        }
        if (currencyDAO.findByCode(baseCode).isEmpty()) {
            throw new ValidationException("Валюта " + baseCode + " не найдена");
        }

        if (currencyDAO.findByCode(targetCode).isEmpty()) {
            throw new ValidationException("Валюта " + targetCode +
                    " не найдена");
        }


    }


}
