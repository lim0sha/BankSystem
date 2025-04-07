package Presentation.DTO;

import Application.Models.Entities.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BankAccountDTO {
    private final Integer id;
    private final Double balance;
    private final String userLogin;
    private final Integer userId;

    public BankAccountDTO(BankAccount account) {
        this.id = account.getId();
        this.balance = account.getBalance();
        this.userLogin = account.getUserLogin();
        this.userId = account.getUser() != null ? account.getUser().getId() : null;
    }
}
