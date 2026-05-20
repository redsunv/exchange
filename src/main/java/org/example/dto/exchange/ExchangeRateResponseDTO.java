package org.example.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.dto.currency.CurrencyResponseDTO;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateResponseDTO {
    private Long id;
    private CurrencyResponseDTO baseCurrency;
    private CurrencyResponseDTO targetCurrency;
    private BigDecimal rate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyResponseDTO getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyResponseDTO baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyResponseDTO getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(CurrencyResponseDTO targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}