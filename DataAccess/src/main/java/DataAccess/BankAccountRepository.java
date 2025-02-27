package DataAccess;

import lombok.NoArgsConstructor;
import Application.Abstractions.Repositories.IBankAccountRepository;
import Application.Contracts.ResultTypes.BankAccountResult;
import Application.Models.Entites.BankAccount;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Репозиторий для управления банковскими счетами.
 * Реализует интерфейс {@link IBankAccountRepository} и выполняет CRUD-операции с объектами {@link BankAccount}.
 *
 * <p>Хранение банковских счетов осуществляется в памяти с использованием {@link ArrayList}.</p>
 */
@NoArgsConstructor
public class BankAccountRepository implements IBankAccountRepository {

    /**
     * Внутреннее хранилище банковских счетов в памяти.
     */
    private final ArrayList<BankAccount> _bankAccounts = new ArrayList<>();

    /**
     * Добавляет новый банковский счет в хранилище.
     *
     * @param account объект банковского счета, который будет добавлен.
     * @return {@link BankAccountResult.Success} при успешном добавлении
     * или {@link BankAccountResult.BankAccountCreationError} в случае ошибки.
     */
    @Override
    public BankAccountResult AddBankAccount(BankAccount account) {
        if (account == null) {
            return new BankAccountResult.BankAccountCreationError("Bank account cannot be null");
        }
        _bankAccounts.add(account);
        return new BankAccountResult.Success();
    }

    /**
     * Ищет банковский счет по идентификатору.
     *
     * @param id идентификатор банковского счета.
     * @return объект {@link BankAccount}, если найден, иначе {@code null}.
     */
    @Override
    public BankAccount FindBankAccountById(Integer id) {
        if (id < 0) {
            return null;
        }
        for (BankAccount account : _bankAccounts) {
            if (Objects.equals(account.getId(), id)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Обновляет баланс банковского счета.
     *
     * @param id идентификатор банковского счета.
     * @param balance новый баланс счета.
     * @return {@link BankAccountResult.Success} при успешном обновлении
     * или {@link BankAccountResult.BankAccountUpdateError} в случае ошибки.
     */
    @Override
    public BankAccountResult UpdateBankAccountBalance(Integer id, Double balance) {
        if (id < 0 || balance < 0) {
            return new BankAccountResult.BankAccountUpdateError("Invalid update parameters");
        }

        for (BankAccount account : _bankAccounts) {
            if (Objects.equals(account.getId(), id)) {
                account.setBalance(balance);
                return new BankAccountResult.Success();
            }
        }

        return new BankAccountResult.BankAccountUpdateError("Bank account balance update failed");
    }

    /**
     * Удаляет банковский счет по идентификатору.
     *
     * @param id идентификатор банковского счета, который нужно удалить.
     * @return {@link BankAccountResult.Success} при успешном удалении
     * или {@link BankAccountResult.BankAccountDeletionError} в случае ошибки.
     */
    @Override
    public BankAccountResult DeleteBankAccount(Integer id) {
        if (id < 0) {
            return new BankAccountResult.BankAccountDeletionError("Invalid delete parameters");
        }
        _bankAccounts.removeIf(bankAccount -> Objects.equals(bankAccount.getId(), id));
        return new BankAccountResult.Success();
    }
}
