package Application.Models.Entites;

import lombok.Getter;
import Application.Models.Enums.OperationType;

@Getter
public class Operation {
    private final Integer BankAccountId;

    private final OperationType Type;

    private final Double Amount;

    public Operation(Integer bankAccountId, OperationType type, Double amount) {
        this.BankAccountId = bankAccountId;
        this.Type = type;
        this.Amount = amount;
    }
}
