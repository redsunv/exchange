package org.example.dto.exchange;

import lombok.*;
import org.example.dto.currency.CurrencyResponseDTO;

import java.math.BigDecimal;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateConversionResponseDTO {
    private CurrencyResponseDTO baseCurrency;
    private CurrencyResponseDTO  targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;


    private BigDecimal convertedAmount;

}
