package org.example.dao;

import org.example.model.Currency;

import java.util.Optional;

public interface CurrencyDAO extends CrudDAO<Currency,Long> {
    Optional<Currency> findByCode(String code);
}
