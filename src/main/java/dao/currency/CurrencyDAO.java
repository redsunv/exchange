package dao.currency;

import model.Currency;

import java.util.Optional;

public interface CurrencyDAO extends CrudDAO<Currency,Long> {
    Optional<Currency> findByCode(String code);
}
