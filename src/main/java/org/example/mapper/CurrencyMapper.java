package org.example.mapper;

import org.example.dto.CurrencyRequestDTO;
import org.example.dto.CurrencyResponseDTO;
import org.example.model.Currency;

public class CurrencyMapper {

    public static Currency toEntity (CurrencyRequestDTO request){
        if (request == null) return null;

        Currency currency = new Currency();

        currency.setCode(request.getCode());
        currency.setFullName(request.getFull_name());
        currency.setSign(request.getSign());

        return currency;
    }

    public static CurrencyResponseDTO toResponseDTO(Currency currency){
        if (currency == null)
            return null;

        CurrencyResponseDTO dto = new CurrencyResponseDTO();
        dto.setId(currency.getId());
        dto.setCode(currency.getCode());
        dto.setFull_name(currency.getFullName());
        dto.setSign(currency.getSign());

        return dto;
    }
}
