package Application.Abstractions.Repositories;

import Application.Contracts.ResultTypes.BankAccountResult;
import Application.Models.Entites.BankAccount;

/**
 * Репозиторий для управления банковскими счетами.
 * Реализует паттерн CRUD для добавления, поиска, обновления и удаления банковских счетов.
 */
public interface IBankAccountRepository {

    /**
     * Добавляет новый банковский счет в хранилище данных.
     *
     * @param account объект банковского счета, который необходимо добавить.
     * @return результат операции добавления банковского счета, может содержать сообщение об ошибке или подтверждение успеха.
     */
    BankAccountResult AddBankAccount(BankAccount account);

    /**
     * Ищет банковский счет в хранилище данных по его идентификатору.
     *
     * @param id идентификатор банковского счета, который нужно найти.
     * @return объект банковского счета, если он существует; {@code null}, если счет не найден.
     */
    BankAccount FindBankAccountById(Integer id);

    /**
     * Обновляет баланс существующего банковского счета.
     *
     * @param id идентификатор банковского счета, баланс которого необходимо обновить.
     * @param balance новый баланс банковского счета.
     * @return результат операции обновления баланса, может содержать сообщение об ошибке или подтверждение успеха.
     */
    BankAccountResult UpdateBankAccountBalance(Integer id, Double balance);

    /**
     * Удаляет банковский счет из хранилища данных по его идентификатору.
     *
     * @param id идентификатор банковского счета, который нужно удалить.
     * @return результат операции удаления банковского счета, может содержать сообщение об ошибке или подтверждение успеха.
     */
    BankAccountResult DeleteBankAccount(Integer id);
}
