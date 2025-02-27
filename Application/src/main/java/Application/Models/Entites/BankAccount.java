package Application.Models.Entites;

import lombok.Getter;
import lombok.Setter;
import Application.Models.Utils.IdGenerator;

/**
 * Класс, представляющий банковский счет пользователя.
 * Хранит информацию о счете, балансе и привязанном пользователе.
 */
@Getter
public class BankAccount {
    final private Integer id;

    @Setter
    private Double balance;

    private final String userLogin;

    private final Integer UserId;

    /**
     * Конструктор для создания нового банковского счета.
     * Генерирует уникальный идентификатор счета и связывает его с пользователем.
     *
     * @param idGenerator генератор ID для создания уникального идентификатора счета.
     * @param user пользователь, к которому будет привязан новый счет.
     */
    public BankAccount(IdGenerator idGenerator, User user) {
        this.id = idGenerator.generateBankAccountId();
        this.balance = 0.0;
        this.userLogin = user.getLogin();
        this.UserId = user.getId();
    }
}
