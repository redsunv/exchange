package dto.currency;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class CurrencyResponseDTO {
    private Long id;
    private String code;
    private String fullName;
    private String sign;

}
