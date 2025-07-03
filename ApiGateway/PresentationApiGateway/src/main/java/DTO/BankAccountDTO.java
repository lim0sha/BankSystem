package DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BankAccountDTO {
    private Integer id;
    private Double balance;
    private String userLogin;
    private Integer userId;
}
