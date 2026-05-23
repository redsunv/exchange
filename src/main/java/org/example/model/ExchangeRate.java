package org.example.model;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    private Long id;
    private Long baseCurrencyId;
    private Long targetCurrencyId;
    private BigDecimal rate;

    private Currency baseCurrency;
    private Currency targetCurrency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(Long baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public Long getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(Long targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }
}