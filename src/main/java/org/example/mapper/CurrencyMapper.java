package org.example.mapper;

import org.example.dto.currency.CurrencyRequestDTO;
import org.example.dto.currency.CurrencyResponseDTO;
import org.example.model.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CurrencyMapper {

    // Request DTO → Entity
    public static Currency fromRequestToEntity(CurrencyRequestDTO request) {
        if (request == null) return null;

        Currency currency = new Currency();
        currency.setCode(request.getCode());
        currency.setFullName(request.getFullName());
        currency.setSign(request.getSign());

        return currency;
    }

    // Entity → Response DTO (один объект)
    public static CurrencyResponseDTO toResponseDTO(Currency currency) {
        if (currency == null) return null;

        CurrencyResponseDTO dto = new CurrencyResponseDTO();
        dto.setId(currency.getId());
        dto.setCode(currency.getCode());
        dto.setFullName(currency.getFullName());
        dto.setSign(currency.getSign());

        return dto;
    }

    // List<Entity> → List<Response DTO>
    public static List<CurrencyResponseDTO> toResponseDTOList(List<Currency> currencies) {
        if (currencies == null) return List.of();

        return currencies.stream()
                .map(CurrencyMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public static Currency fromResultSet(ResultSet rs) throws SQLException {
        Currency currency = new Currency();
        currency.setId(rs.getLong("id"));
        currency.setCode(rs.getString("code"));
        currency.setFullName(rs.getString("fullName"));
        currency.setSign(rs.getString("sign"));
        return currency;
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
