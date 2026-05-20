package org.example.validator;


import org.example.exception.DatabaseAccessException;

public class ExchangeRateValidator {

    public void validateDifferentPairs(Long baseCurrencyId, Long targetCurrencyId){
        if (baseCurrencyId.equals(targetCurrencyId)){
            throw new DatabaseAccessException("The base and target currencies must be different");
        }

    }

}
