package dto.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CurrencyRequestDTO {
    private String code;
    private String fullName;
    private String sign;


}
