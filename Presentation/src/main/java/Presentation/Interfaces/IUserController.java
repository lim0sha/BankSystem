package Presentation.Interfaces;

import Application.ResultTypes.BankAccountResult;
import Application.ResultTypes.OperationResult;
import Application.ResultTypes.UserResult;
import Application.Models.Entities.BankAccount;
import Application.Models.Entities.User;

/**
 * Интерфейс, предоставляющий операции для управления пользователями и их банковскими счетами.
 * Включает методы для создания, удаления пользователей, управления их друзьями и банковскими счетами,
 * а также выполнения различных операций с банковскими счетами.
 */
public interface IUserController {

    /**
     * Создание нового пользователя.
     *
     * @param user объект пользователя, который должен быть создан.
     * @return результат операции создания пользователя.
     */
    UserResult CreateUser(User user);

    /**
     * Создание нового пользователя.
     *
     * @param id идентификатор пользователя, который должен быть найден.
     * @return результат операции поиска пользователя.
     */
    User GetUserById(int id);

    /**
     * Создание нового пользователя.
     *
     * @param id идентификатор аккаунта, который должен быть найден.
     * @return результат операции поиска аккаунта.
     */
    BankAccount GetBankAccountById(int id);

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
     * @param id идентификатор пользователя, информацию о котором необходимо получить.
     */
    void GetUserInfo(int id);

    /**
     * Добавление пользователя в друзья.
     *
     * @param userId идентификатор текущего пользователя.
     * @param otherId идентификатор пользователя, которого нужно добавить в друзья.
     */
    void AddFriend(int userId, int otherId);

    /**
     * Удаление пользователя из списка друзей.
     *
     * @param userId идентификатор текущего пользователя.
     * @param otherId идентификатор пользователя, которого нужно удалить из друзей.
     */
    void RemoveFriend(int userId, int otherId);

    /**
     * Добавление нового банковского счета пользователю.
     *
     * @param userId идентификатор пользователя, которому нужно добавить банковский счет.
     * @param bankAccount объект банковского счета, который нужно добавить.
     * @return результат операции добавления банковского счета.
     */
    BankAccountResult addBankAccount(int userId, BankAccount bankAccount);

    /**
     * Удаление банковского счета пользователя.
     *
     * @param userId идентификатор пользователя, чей банковский счет должен быть удален.
     * @param bankAccount объект банковского счета, который нужно удалить.
     * @return результат операции удаления банковского счета.
     */
    BankAccountResult RemoveBankAccount(int userId, BankAccount bankAccount);

    /**
     * Проверка баланса банковского счета пользователя.
     *
     * @param userId идентификатор пользователя, чей баланс нужно проверить.
     * @param bankAccountId объект банковского счета, баланс которого нужно проверить.
     * @return результат операции проверки баланса.
     */
    OperationResult CheckBalance(int userId, int bankAccountId);

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
     * @param bankAccountId идентификатор банковского счета, для которого нужно получить историю операций.
     * @return результат операции получения истории.
     */
    OperationResult GetOperationHistory(int bankAccountId);
}
