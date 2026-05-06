package dao;

import java.util.List;
import java.util.Optional;

public class CurrencyDAOImpl implements CurrencyDAO{


    @Override
    public Optional<Currency> findByCode(String code) {
        return Optional.empty();
    }

    @Override
    public List<Currency> getAllCurrencies(Long aLong) {
        return List.of();
    }

    @Override
    public Optional<Currency> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Currency save(Currency currency) {
        return null;
    }

    @Override
    public Optional<Currency> update(Currency currency) {
        return Optional.empty();
    }

    @Override
    public void delete(Long aLong) {

    }
}
