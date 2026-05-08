package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResponseDTO {
    private Long id;
    private String code;
    private String full_name;
    private String sign;


}
