package org.example.mapper;

import org.example.dto.CurrencyRequestDTO;
import org.example.dto.CurrencyResponseDTO;
import org.example.model.Currency;
import java.util.List;
import java.util.stream.Collectors;

public class CurrencyMapper {

    // Request DTO → Entity
    public static Currency toEntity(CurrencyRequestDTO request) {
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
}