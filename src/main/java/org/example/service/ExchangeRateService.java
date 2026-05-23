package org.example.service;

import org.example.dao.currency.CurrencyDAO;
import org.example.dao.currency.CurrencyDAOImpl;
import org.example.dao.exchange.ExchangeRateDAO;
import org.example.dao.exchange.JdbcExchangeRateDaoImpl;
import org.example.dto.exchange.ExchangeRateResponseDTO;
import org.example.exception.NotFoundException;
import org.example.mapper.ExchangeRateMapper;
import org.example.model.Currency;
import org.example.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    private final ExchangeRateDAO exchangeRateDAO = new JdbcExchangeRateDaoImpl();
    private final CurrencyDAO currencyDAO = new CurrencyDAOImpl();

    public List<ExchangeRateResponseDTO> getAllExchangeRates() {
        List<ExchangeRate> rates = exchangeRateDAO.getAll();
        return ExchangeRateMapper.toResponseDTOList(rates);
    }

    public ExchangeRateResponseDTO createNewExchangeRate(String baseCode, String targetCode, BigDecimal rate) {
        //существует ли валюта
        //разные ли валюты
        //есть ли курс

        Optional<Currency> currencyBase = currencyDAO.findByCode(baseCode);
        if (currencyBase.isEmpty()) throw new NotFoundException("Валюта " + baseCode + " не найдена");

        Optional<Currency> currencyTarget = currencyDAO.findByCode(targetCode);
        if (currencyTarget.isEmpty()) throw new NotFoundException("Валюта " + targetCode + " не найдена");


        return ExchangeRateMapper.toResponseDTO();
    }
}

