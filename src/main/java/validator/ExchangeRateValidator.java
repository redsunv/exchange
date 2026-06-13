package validator;


import dao.currency.CurrencyDAO;
import dao.exchange.ExchangeRateDAO;
import exception.DatabaseAccessException;
import exception.ValidationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ExchangeRateValidator {

    public static void validateDifferentPairsById(Long baseCurrencyId, Long targetCurrencyId) {
        if (baseCurrencyId.equals(targetCurrencyId)) {
            throw new DatabaseAccessException("Валюты должны быть разными");
        }

    }

    public static void validateDifferentPairs(String baseCode, String targetCode) {
        if (baseCode.equals(targetCode)) {
            throw new DatabaseAccessException("Валюты должны быть разными");
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

        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Введите корректный курс");
        }


    }

    public static List<String> validateExchangeRateCode(String baseCode, String targetCode) {
        List<String> errors = new ArrayList<>();

        if (baseCode == null || baseCode.isBlank()) {
            errors.add("Требуется код валюты");
        } else {
            if (baseCode.length() != 3) {
                errors.add("Код валюты должен состоять из 3-х символов");
            }
            if (!baseCode.matches("[A-Z]{3}")) {
                errors.add("Код валюты должен состоять из  заглавных букв (A-Z)");
            }
        }
        if (targetCode == null || targetCode.isBlank()) {
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

    }

    public static List<String> validateRequestParameters(String from, String to, String amount) {
        List<String> errors = new ArrayList<>();

        if (from == null || from.isBlank()) {
            errors.add("Нет параметров: from");
        } else if (from.length() != 3 || !from.matches("[A-Z]{3}")) {
            errors.add("Код валюты " + from + "должен состоять из 3-х букв");
        }

        if (to == null || to.isBlank()) {
            errors.add("Нет параметров: to");
        } else if (to.length() != 3 || !to.matches("[A-Z]{3}")) {
            errors.add("Код валюты " + to + "должен состоять из 3-х букв");
        }
        if (from != null && from.equals(to)) {
            errors.add("Валюты должны быть разными");
        }
        if (amount == null || amount.isBlank()) {
            errors.add("Нет параметров: amount");
        } else {
            try {
                BigDecimal amountValue = new BigDecimal(amount);
                if (amountValue.compareTo(BigDecimal.ZERO) <= 0) {
                    errors.add("Amount должно быть больше 0");
                }
            } catch (NumberFormatException e) {
                errors.add("Неверный формат " + amount);
            }
        }
        return errors;
    }


}

