package Application.Models.Entities;

import jakarta.persistence.*;
import lombok.*;
import Application.Models.Enums.OperationType;

/**
 * Класс, представляющий операцию с банковским счетом.
 * Хранит информацию о типе операции, сумме и счете, на котором она была проведена.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private BankAccount bankAccount;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String type;

    /**
     * Конструктор для создания новой операции.
     *
     * @param bankAccount банковский счет, на котором выполнена операция.
     * @param type тип операции (например, депозит или снятие).
     * @param amount сумма, участвующая в операции.
     */
    public Operation(BankAccount bankAccount, OperationType type, Double amount) {
        this.bankAccount = bankAccount;
        this.type = type.toString();
        this.amount = amount;
    }
}
