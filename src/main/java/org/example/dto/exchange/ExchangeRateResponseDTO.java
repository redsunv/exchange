package org.example.dto.exchange;

import lombok.Data;
import org.example.dto.currency.CurrencyResponseDTO;

import java.math.BigDecimal;
@Data
public class ExchangeRateResponseDTO {
    private Long id;
    private CurrencyResponseDTO baseCurrency;
    private CurrencyResponseDTO targetCurrency;
    private BigDecimal rate;
}