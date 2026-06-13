package mapper;

import dto.exchange.ExchangeRateResponseDTO;
import model.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeRateMapper {


    public static ExchangeRate fromResultSetFull(ResultSet rs) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate();

        exchangeRate.setId(rs.getLong("id"));
        exchangeRate.setRate(rs.getBigDecimal("rate"));


        exchangeRate.setBaseCurrency(CurrencyMapper.fromResultSetWithPrefix(rs, "base_"));
        exchangeRate.setTargetCurrency(CurrencyMapper.fromResultSetWithPrefix(rs, "target_"));

        return exchangeRate;
    }

    public static ExchangeRateResponseDTO toResponseDTO(ExchangeRate exchangeRate) {
        if (exchangeRate == null) return null;
        ExchangeRateResponseDTO responseDTO = new ExchangeRateResponseDTO();

        responseDTO.setId(exchangeRate.getId());
        responseDTO.setRate(exchangeRate.getRate());

        responseDTO.setBaseCurrency(CurrencyMapper.toResponseDTO(exchangeRate.getBaseCurrency()));
        responseDTO.setTargetCurrency(CurrencyMapper.toResponseDTO(exchangeRate.getTargetCurrency()));

        return responseDTO;
    }

    public static List<ExchangeRateResponseDTO> toResponseDTOList(List<ExchangeRate> exchangeRates) {
        if (exchangeRates == null || exchangeRates.isEmpty())
            return List.of();
        return exchangeRates.stream().map(ExchangeRateMapper::toResponseDTO).collect(Collectors.toList());
    }


    }


