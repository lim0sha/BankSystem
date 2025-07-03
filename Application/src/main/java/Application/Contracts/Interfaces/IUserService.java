package Application.Contracts.Interfaces;

import Application.Contracts.ResultTypes.BankAccountResult;
import Application.Contracts.ResultTypes.OperationResult;
import Application.Contracts.ResultTypes.UserResult;
import Application.Models.Entites.BankAccount;
import Application.Models.Entites.User;

/**
 * Интерфейс, предоставляющий операции для управления пользователями и их банковскими счетами.
 * Включает методы для создания, удаления пользователей, управления их друзьями и банковскими счетами,
 * а также выполнения различных операций с банковскими счетами.
 */
public interface IUserService {

    /**
     * Создание нового пользователя.
     *
     * @param user объект пользователя, который должен быть создан.
     * @return результат операции создания пользователя.
     */
    UserResult CreateUser(User user);

    /**
     * Удаление пользователя.
     *
     * @param user объект пользователя, который должен быть удален.
     * @return результат операции удаления пользователя.
     */
    UserResult DeleteUser(User user);

    /**
     * Получение информации о пользователе.
     *
     * @param user объект пользователя, информацию о котором необходимо получить.
     */
    void GetUserInfo(User user);

    /**
     * Добавление пользователя в друзья.
     *
     * @param user объект текущего пользователя.
     * @param other объект пользователя, которого нужно добавить в друзья.
     */
    void AddFriend(User user, User other);

    /**
     * Удаление пользователя из списка друзей.
     *
     * @param user объект текущего пользователя.
     * @param other объект пользователя, которого нужно удалить из друзей.
     */
    void RemoveFriend(User user, User other);

    /**
     * Добавление нового банковского счета пользователю.
     *
     * @param user объект пользователя, которому нужно добавить банковский счет.
     * @param bankAccount объект банковского счета, который нужно добавить.
     * @return результат операции добавления банковского счета.
     */
    BankAccountResult addBankAccount(User user, BankAccount bankAccount);

    /**
     * Удаление банковского счета пользователя.
     *
     * @param user объект пользователя, чей банковский счет должен быть удален.
     * @param bankAccount объект банковского счета, который нужно удалить.
     * @return результат операции удаления банковского счета.
     */
    BankAccountResult RemoveBankAccount(User user, BankAccount bankAccount);

    /**
     * Проверка баланса банковского счета пользователя.
     *
     * @param user объект пользователя, чей баланс нужно проверить.
     * @param bankAccount объект банковского счета, баланс которого нужно проверить.
     * @return результат операции проверки баланса.
     */
    OperationResult CheckBalance(User user, BankAccount bankAccount);

    /**
     * Пополнение банковского счета.
     *
     * @param bankAccount объект банковского счета, на который нужно пополнить средства.
     * @param amount сумма, которую нужно внести на счет.
     * @return результат операции пополнения.
     */
    OperationResult Deposit(BankAccount bankAccount, Double amount);

    /**
     * Снятие средств с банковского счета.
     *
     * @param bankAccount объект банковского счета, с которого нужно снять средства.
     * @param amount сумма, которую нужно снять со счета.
     * @return результат операции снятия средств.
     */
    OperationResult Withdraw(BankAccount bankAccount, Double amount);

    /**
     * Перевод средств между двумя банковскими счетами.
     *
     * @param bankAccount1 объект исходного банковского счета.
     * @param bankAccount2 объект целевого банковского счета.
     * @param amount сумма, которую нужно перевести.
     * @return результат операции перевода.
     */
    OperationResult Transfer(BankAccount bankAccount1, BankAccount bankAccount2, Double amount);

    /**
     * Получение истории операций для конкретного банковского счета.
     *
     * @param bankAccount объект банковского счета, для которого нужно получить историю операций.
     * @return результат операции получения истории.
     */
    OperationResult GetOperationHistory(BankAccount bankAccount);
}
