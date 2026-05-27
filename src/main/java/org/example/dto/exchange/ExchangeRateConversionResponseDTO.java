package org.example.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.currency.CurrencyResponseDTO;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateConversionResponseDTO {
    private CurrencyResponseDTO baseCurrency;
    private CurrencyResponseDTO  targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;


    private BigDecimal convertedAmount;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
