package org.example.validator;


import org.example.dao.currency.CurrencyDAO;
import org.example.dao.exchange.ExchangeRateDAO;
import org.example.exception.DatabaseAccessException;
import org.example.exception.ValidationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateValidator {

    public void validateDifferentPairs(Long baseCurrencyId, Long targetCurrencyId) {
        if (baseCurrencyId.equals(targetCurrencyId)) {
            throw new DatabaseAccessException("The base and target currencies must be different");
        }

    }

    public static void validateCreateExchangeRate(String baseCode, String targetCode, BigDecimal rate,
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

        if (rate == null|| rate.compareTo(BigDecimal.ZERO) <= 0){
            throw new ValidationException("Введите корректный курс");
        }


    }
    public static List<String> validateExchangeRateCode(String baseCode, String targetCode){
        List<String > errors = new ArrayList<>();

        if (baseCode == null || baseCode.isBlank()) {
            errors.add("Требуется код валюты");
        } else {
            if (baseCode.length() != 3) {
                errors.add("Код валюты должен состоять из 3-х символов");
            }
            if (!baseCode.matches("[A-Z]{3}")) {
                errors.add("Код валюты должен состоять из  заглавных букв (A-Z)");
            }
        }  if (targetCode == null || targetCode.isBlank()) {
            errors.add("Требуется код валюты");
        } else {
            if (targetCode.length() != 3) {
                errors.add("Код валюты должен состоять из 3-х символов");
            }
            if (!targetCode.matches("[A-Z]{3}")) {
                errors.add("Код валюты должен состоять из  заглавных букв (A-Z)");
            }
        }


        return errors;
    }}


