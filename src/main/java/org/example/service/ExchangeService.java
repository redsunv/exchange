package org.example.service;

import org.example.dao.exchange.ExchangeRateDAO;
import org.example.dao.exchange.JdbcExchangeRateDaoImpl;
import org.example.dto.exchange.ExchangeRateConversionResponseDTO;
import org.example.dto.exchange.ExchangeRateRequestDTO;
import org.example.exception.NotFoundException;
import org.example.mapper.CurrencyMapper;
import org.example.model.ExchangeRate;

import java.math.BigDecimal;

import java.math.RoundingMode;
import java.util.Optional;


public class ExchangeService {
    ExchangeRateDAO exchangeRateDAO = new JdbcExchangeRateDaoImpl();


    public ExchangeRateConversionResponseDTO exchange(ExchangeRateRequestDTO requestDTO) {
        ExchangeRate exchangeRate = findExchangeRate(requestDTO)
                .orElseThrow(() -> new NotFoundException("Обменный курс не найден"));


        BigDecimal amount = requestDTO.getAmount();
        BigDecimal convertedAmount = amount.multiply(exchangeRate.getRate())
                .setScale(2, RoundingMode.HALF_UP);

        ExchangeRateConversionResponseDTO conversionResponseDTO = new ExchangeRateConversionResponseDTO();
        conversionResponseDTO.setBaseCurrency(CurrencyMapper.toResponseDTO(exchangeRate.getBaseCurrency()));
        conversionResponseDTO.setTargetCurrency(CurrencyMapper.toResponseDTO(exchangeRate.getTargetCurrency()));
        conversionResponseDTO.setRate(exchangeRate.getRate());
        conversionResponseDTO.setAmount(amount);
        conversionResponseDTO.setConvertedAmount(convertedAmount);
        return conversionResponseDTO;
    }

    private Optional<ExchangeRate> findExchangeRate(ExchangeRateRequestDTO requestDTO) {

        Optional<ExchangeRate> exchangeRate = findByDirectRate(requestDTO);

        if (exchangeRate.isEmpty()) {
            exchangeRate = findByIndirectRate(requestDTO);

        }
        if (exchangeRate.isEmpty()) {
            exchangeRate = findByCrossRate(requestDTO);
        }

        return exchangeRate;
    }

    private Optional<ExchangeRate> findByCrossRate(ExchangeRateRequestDTO requestDTO) {
        Optional<ExchangeRate> usdToBaseCurrency = exchangeRateDAO.findByCode("USD", requestDTO.getBaseCurrency());
        Optional<ExchangeRate> usdToTargetCurrency = exchangeRateDAO.findByCode("USD", requestDTO.getTargetCurrency());

        if (usdToBaseCurrency.isEmpty() || usdToTargetCurrency.isEmpty()) {
            return Optional.empty();
        }
        ExchangeRate usdBase = usdToBaseCurrency.get();
        ExchangeRate usdTarget = usdToTargetCurrency.get();

        BigDecimal fromToUsd = BigDecimal.ONE.divide(usdBase.getRate(), 6, RoundingMode.HALF_UP);
        BigDecimal rate = fromToUsd.multiply(usdTarget.getRate());
        ExchangeRate crossRate = new ExchangeRate();

        crossRate.setBaseCurrency(usdBase.getTargetCurrency());
        crossRate.setTargetCurrency(usdTarget.getTargetCurrency());
        crossRate.setRate(rate);

        return Optional.of(crossRate);
    }

    private Optional<ExchangeRate> findByIndirectRate(ExchangeRateRequestDTO requestDTO) {
        String baseCurrency = requestDTO.getTargetCurrency();
        String targetCurrency = requestDTO.getBaseCurrency();

        Optional<ExchangeRate> reverseRate = exchangeRateDAO.findByCode(baseCurrency, targetCurrency);
        if (reverseRate.isEmpty()) {
            return Optional.empty();

        }
        ExchangeRate reverse = reverseRate.get();

        ExchangeRate directRate = new ExchangeRate();

        directRate.setBaseCurrency(reverse.getTargetCurrency());
        directRate.setTargetCurrency(reverse.getBaseCurrency());
        directRate.setRate(BigDecimal.ONE.divide(reverse.getRate(), 6, RoundingMode.HALF_UP));

        return Optional.of(directRate);
    }

    private Optional<ExchangeRate> findByDirectRate(ExchangeRateRequestDTO requestDTO) {

        return exchangeRateDAO.findByCode(requestDTO.getBaseCurrency(), requestDTO.getTargetCurrency());
    }

}
