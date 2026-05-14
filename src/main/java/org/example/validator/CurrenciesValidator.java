package org.example.validator;

import java.util.ArrayList;
import java.util.List;

public class CurrenciesValidator {
    public static List<String> validateCurrenciesCode(String code, String fullName, String sign){
        List<String > errors = new ArrayList<>();

        if (code == null || code.isBlank()) {
            errors.add("Требуется код валюты");
        } else {
            if (code.length() != 3) {
                errors.add("Код валюты должен состоять из 3-х символов");
            }
            if (!code.matches("[A-Z]{3}")) {
                errors.add("Код валюты должен состоять из  заглавных букв (A-Z)");
            }
        }

        if (fullName == null || fullName.isBlank()){
            errors.add("Требуется название валюты");
        }

        if (sign == null || sign.isBlank()){
            errors.add("Требуется символ валюты");
        }
        return errors;
    }
}
