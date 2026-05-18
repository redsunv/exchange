package org.example.mapper;

import org.example.dto.exchange.ExchangeRateRequestDTO;
import org.example.model.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeRatesMapper {

    public static ExchangeRate fromResultSet(ResultSet rs) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setId(rs.getLong("id"));
        exchangeRate.setBaseCurrencyId(rs.getLong("base_currency_id"));
        exchangeRate.setTargetCurrencyId(rs.getLong("target_currency_id"));
        exchangeRate.setRate(rs.getBigDecimal("rate"));
        return exchangeRate;
    }

    public static ExchangeRate fromResultSetFull(ResultSet rs) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate();

        exchangeRate.setId(rs.getLong("id"));
        exchangeRate.setRate(rs.getBigDecimal("rate"));


        exchangeRate.setBaseCurrency(CurrencyMapper.fromResultSetWithPrefix(rs, "base_"));
        exchangeRate.setTargetCurrency(CurrencyMapper.fromResultSetWithPrefix(rs, "target_"));

        return exchangeRate;
    }

    public static ExchangeRateRequestDTO toResponseDTO(ExchangeRate exchangeRate){
        if (exchangeRate == null) return null;

    }

}