package org.example.dao.currency;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<Currency, ID> {

    List<Currency> getAllCurrencies();

    Optional<Currency> findById(ID id);//альтернатива null

    Currency save(Currency currency);

    Optional<Currency> update(Currency currency);

    void delete(ID id);

}
