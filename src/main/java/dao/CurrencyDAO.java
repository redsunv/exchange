package dao;

import java.util.Optional;

public interface CurrencyDAO extends CrudDAO<Currency,Long> {
    Optional<Currency> findByCode(String code);
}
