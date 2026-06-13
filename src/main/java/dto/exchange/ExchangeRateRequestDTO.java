package dto.exchange;

import lombok.*;

import java.math.BigDecimal;
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequestDTO {
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal amount;

}
