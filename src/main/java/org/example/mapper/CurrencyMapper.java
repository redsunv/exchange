package org.example.mapper;

import org.example.dto.currency.CurrencyRequestDTO;
import org.example.dto.currency.CurrencyResponseDTO;
import org.example.model.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CurrencyMapper {

    public static CurrencyResponseDTO toResponseDTO(Currency currency) {
        if (currency == null) return null;

        CurrencyResponseDTO dto = new CurrencyResponseDTO();
        dto.setId(currency.getId());
        dto.setCode(currency.getCode());
        dto.setFullName(currency.getFullName());
        dto.setSign(currency.getSign());

        return dto;
    }


    public static List<CurrencyResponseDTO> toResponseDTOList(List<Currency> currencies) {
        if (currencies == null || currencies.isEmpty()) return List.of();

        return currencies.stream()
                .map(CurrencyMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    public static Currency fromResultSetWithPrefix(ResultSet rs, String prefix) throws SQLException {
        Currency currency = new Currency();
        currency.setId(rs.getLong(prefix + "id"));
        currency.setCode(rs.getString(prefix + "code"));
        currency.setFullName(rs.getString(prefix + "fullName"));
        currency.setSign(rs.getString(prefix + "sign"));
        return currency;
    }
}
