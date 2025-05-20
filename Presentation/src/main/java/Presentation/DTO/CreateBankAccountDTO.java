package Presentation.DTO;

import lombok.Data;

@Data
public class CreateBankAccountDTO {
    private Integer id;
    private double balance;
    private Integer userId;
}
