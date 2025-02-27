package Application.Models.Entites;

import lombok.Getter;
import Application.Models.Enums.OperationType;

/**
 * Класс, представляющий операцию с банковским счетом.
 * Хранит информацию о типе операции, сумме и счете, на котором она была проведена.
 */
@Getter
public class Operation {
    private final Integer BankAccountId;

    private final OperationType Type;

    private final Double Amount;

    /**
     * Конструктор для создания новой операции.
     *
     * @param bankAccountId идентификатор банковского счета, на котором выполнена операция.
     * @param type тип операции (например, депозит или снятие).
     * @param amount сумма, участвующая в операции.
     */
    public Operation(Integer bankAccountId, OperationType type, Double amount) {
        this.BankAccountId = bankAccountId;
        this.Type = type;
        this.Amount = amount;
    }
}
