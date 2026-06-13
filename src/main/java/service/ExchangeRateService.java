package service;

import dao.currency.CurrencyDAO;
import dao.currency.CurrencyDAOImpl;
import dao.exchange.ExchangeRateDAO;
import dao.exchange.JdbcExchangeRateDaoImpl;
import dto.exchange.ExchangeRateResponseDTO;
import exception.DatabaseAccessException;
import exception.NotFoundException;
import mapper.ExchangeRateMapper;
import model.Currency;
import model.ExchangeRate;
import validator.ExchangeRateValidator;

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

    public ExchangeRateResponseDTO getExchangeRate(String baseCode, String targetCode) {
        ExchangeRateValidator.validateDifferentPairs(baseCode, targetCode);
        ExchangeRate exchangeRate = exchangeRateDAO.findByCode(baseCode, targetCode)
                .orElseThrow(() -> new NotFoundException("Обменный курс для пары " + baseCode + targetCode + " не найден"));

        return ExchangeRateMapper.toResponseDTO(exchangeRate);
    }

    public ExchangeRateResponseDTO upDateExchangeRate(String baseCode, String targetCode, BigDecimal rate){
        ExchangeRateValidator.validateDifferentPairs(baseCode, targetCode);
        ExchangeRate exchangeRate = exchangeRateDAO.findByCode(baseCode, targetCode)
                .orElseThrow(() -> new NotFoundException("Обменный курс для пары " + baseCode + targetCode + " не найден"));

        exchangeRate.setRate(rate);
        Optional<ExchangeRate> updated =exchangeRateDAO.update(exchangeRate);
        ExchangeRate newRate = updated.orElseThrow(() -> new DatabaseAccessException("Ошибка обновления"));

        return ExchangeRateMapper.toResponseDTO(newRate);
    }
    }


