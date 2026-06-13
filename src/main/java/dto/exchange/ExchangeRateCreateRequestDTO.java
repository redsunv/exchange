package dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateCreateRequestDTO {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal rate;

}
