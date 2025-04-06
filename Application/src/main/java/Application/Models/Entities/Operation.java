package Application.Models.Entities;

import jakarta.persistence.*;
import lombok.*;
import Application.Models.Enums.OperationType;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccount bankAccount;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "type", nullable = false)
    private String type;

    public Operation(BankAccount bankAccount, OperationType type, Double amount) {
        this.bankAccount = bankAccount;
        this.type = type.toString();
        this.amount = amount;
    }
}
