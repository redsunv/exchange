package model;

import lombok.*;


@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    private Long id;
    private String code;
    private String fullName;
    private String sign;


}
