package org.example.validator;

import java.util.ArrayList;
import java.util.List;

public class CurrencyValidator {
    public static List<String> validateCurrencyCode(String code){
        List<String> errors = new ArrayList<>();

        if (code == null || code.isBlank()) {
            errors.add("Требуется код валюты");
        } else {
            if (code.length() != 3) {
                errors.add("Код валюты должен состоять из 3-х символов");
            }
            if (!code.matches("[A-Z]{3}")) {
                errors.add("Код валюты должен состоять из 3-x заглавных букв (A-Z)");
            }
        }

        return errors;
    }

}
