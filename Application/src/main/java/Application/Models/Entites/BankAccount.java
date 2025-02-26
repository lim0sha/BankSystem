package Application.Models.Entites;

import lombok.Getter;
import lombok.Setter;
import Application.Models.Utils.IdGenerator;

@Getter
public class BankAccount {
    final private Integer id;

    @Setter
    private Double balance;

    private final String userLogin;

    private final Integer UserId;

    public BankAccount(IdGenerator idGenerator, User user) {
        this.id = idGenerator.generateBankAccountId();
        this.balance = 0.0;
        this.userLogin = user.getLogin();
        this.UserId = user.getId();
    }
}
