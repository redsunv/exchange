package org.example.dto.exchange;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ExchangeRateRequestDTO {
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal amount;
}
