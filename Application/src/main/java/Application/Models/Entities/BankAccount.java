package Application.Models.Entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Класс, представляющий банковский счет пользователя.
 * Хранит информацию о счете, балансе и привязанном пользователе.
 */
@Entity
@Table(name = "BankAccounts")
@Getter
@NoArgsConstructor
public class BankAccount {
    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    private Double balance;

    @Column(name = "userLogin")
    private String userLogin;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    /**
     * Конструктор для создания нового банковского счета.
     * Генерирует уникальный идентификатор счета и связывает его с пользователем.
     *
     * @param user пользователь, к которому будет привязан новый счет.
     */
    public BankAccount(User user) {
        this.balance = 0.0;
        this.userLogin = user.getLogin();
        this.user = user;
    }
}
