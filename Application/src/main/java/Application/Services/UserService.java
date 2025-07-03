package Application.Services;

import lombok.AllArgsConstructor;
import Application.Abstractions.Repositories.IBankAccountRepository;
import Application.Abstractions.Repositories.IOperationRepository;
import Application.Abstractions.Repositories.IUserRepository;
import Application.Contracts.Interfaces.IUserService;
import Application.Contracts.ResultTypes.BankAccountResult;
import Application.Contracts.ResultTypes.OperationResult;
import Application.Contracts.ResultTypes.UserResult;
import Application.Managers.UserManager;
import Application.Models.Entites.BankAccount;
import Application.Models.Entites.Operation;
import Application.Models.Entites.User;
import Application.Models.Enums.OperationType;
import lombok.Getter;

/**
 * Сервис для управления пользователями, их банковскими счетами и операциями.
 * Взаимодействует с репозиториями пользователей, банковских счетов и операций.
 * Обрабатывает запросы, такие как создание/удаление пользователей, переводы, операции с балансом и так далее.
 */
@AllArgsConstructor
public class UserService implements IUserService {
    
    @Getter
    private IUserRepository _userRepository;
    
    @Getter
    private IBankAccountRepository _bankAccountRepository;
    
    @Getter
    private IOperationRepository _operationRepository;
    
    private UserManager _userManager;

    /**
     * Создает нового пользователя.
     *
     * @param user объект пользователя, который нужно добавить.
     * @return result-type добавления пользователя.
     */
    @Override
    public UserResult CreateUser(User user) {
        return _userRepository.AddUser(user);
    }

    /**
     * Удаляет существующего пользователя.
     *
     * @param user объект пользователя, которого нужно удалить.
     * @return result-type удаления пользователя.
     */
    @Override
    public UserResult DeleteUser(User user) {
        return _userRepository.DeleteUser(user.getId());
    }

    /**
     * Получает информацию о пользователе и выводит ее.
     *
     * @param user объект пользователя, информацию о котором нужно вывести.
     */
    @Override
    public void GetUserInfo(User user) {
        _userManager.GetUserInfo(user);
    }

    /**
     * Добавляет другого пользователя в список друзей.
     *
     * @param user текущий пользователь, добавляющий друга.
     * @param other пользователь, который будет добавлен в друзья.
     */
    @Override
    public void AddFriend(User user, User other) {
        _userManager.AddFriend(user, other);
    }

    /**
     * Удаляет пользователя из списка друзей.
     *
     * @param user текущий пользователь, удаляющий друга.
     * @param other пользователь, которого нужно удалить из друзей.
     */
    @Override
    public void RemoveFriend(User user, User other) {
        _userManager.RemoveFriend(user, other);
    }

    /**
     * Добавляет новый банковский счет пользователю.
     *
     * @param user пользователь, которому добавляется банковский счет.
     * @param bankAccount объект банковского счета, который нужно добавить.
     * @return result-type добавления счета.
     */
    @Override
    public BankAccountResult addBankAccount(User user, BankAccount bankAccount) {
        _userManager.AddBankAccount(user, bankAccount);
        return _bankAccountRepository.AddBankAccount(bankAccount);
    }

    /**
     * Удаляет банковский счет у пользователя.
     *
     * @param user пользователь, у которого нужно удалить банковский счет.
     * @param bankAccount объект банковского счета, который нужно удалить.
     * @return result-type удаления счета.
     */
    @Override
    public BankAccountResult RemoveBankAccount(User user, BankAccount bankAccount) {
        _userManager.RemoveBankAccount(user, bankAccount);
        return _bankAccountRepository.DeleteBankAccount(bankAccount.getId());
    }

    /**
     * Проверяет баланс на банковском счете пользователя.
     *
     * @param user пользователь, чьи средства нужно проверить.
     * @param bankAccount объект банковского счета, на котором нужно проверить баланс.
     * @return result-type операции (успех или ошибка).
     */
    @Override
    public OperationResult CheckBalance(User user, BankAccount bankAccount) {
        _userManager.CheckBalance(user, bankAccount);
        return _operationRepository.AddOperation(new Operation(bankAccount.getId(), OperationType.CheckBalance, 0.0));
    }

    /**
     * Пополняет банковский счет на указанную сумму.
     *
     * @param bankAccount объект банковского счета, на который нужно пополнить средства.
     * @param amount сумма пополнения.
     * @return result-type операции (успех или ошибка).
     */
    @Override
    public OperationResult Deposit(BankAccount bankAccount, Double amount) {
        var account = _bankAccountRepository.FindBankAccountById(bankAccount.getId());
        _bankAccountRepository.UpdateBankAccountBalance(account.getId(), account.getBalance() + amount);
        return _operationRepository.AddOperation(new Operation(bankAccount.getId(), OperationType.Deposit, amount));
    }

    /**
     * Снимает средства с банковского счета.
     *
     * @param bankAccount объект банковского счета, с которого нужно снять деньги.
     * @param amount сумма снятия.
     * @return result-type операции (успех или ошибка).
     */
    @Override
    public OperationResult Withdraw(BankAccount bankAccount, Double amount) {
        var account = _bankAccountRepository.FindBankAccountById(bankAccount.getId());
        if (account.getBalance() - amount >= 0) {
            _bankAccountRepository.UpdateBankAccountBalance(account.getId(), account.getBalance() - amount);
            return _operationRepository.AddOperation(new Operation(bankAccount.getId(), OperationType.Withdraw, amount));
        }
        return new OperationResult.OperationError("Not enough balance");
    }

    /**
     * Переводит средства между двумя банковскими счетами.
     * В зависимости от типа отношений между пользователями применяется комиссия.
     *
     * @param bankAccount1 первый банковский счет (откуда переводятся деньги).
     * @param bankAccount2 второй банковский счет (куда переводятся деньги).
     * @param amount сумма перевода.
     * @return result-type операции (успех или ошибка).
     */
    @Override
    public OperationResult Transfer(BankAccount bankAccount1, BankAccount bankAccount2, Double amount) {
        if (amount == null || amount <= 0) {
            return new OperationResult.OperationError("Invalid transfer amount.");
        }

        var account1 = _bankAccountRepository.FindBankAccountById(bankAccount1.getId());
        var account2 = _bankAccountRepository.FindBankAccountById(bankAccount2.getId());

        if (account1 == null || account2 == null) {
            return new OperationResult.OperationError("One or both bank accounts not found.");
        }

        var user1 = _userRepository.FindUserById(account1.getUserId());
        var user2 = _userRepository.FindUserById(account2.getUserId());

        if (user1 == null || user2 == null) {
            return new OperationResult.OperationError("One or both users not found.");
        }

        double commissionRate;

        if (account1.getUserId().equals(account2.getUserId())) {
            commissionRate = 0.00;
        } else if (user1.getFriends().stream().anyMatch(friend -> friend.getId().equals(user2.getId()))) {
            commissionRate = 0.03;
        } else {
            commissionRate = 0.10;
        }

        double commission = amount * commissionRate;
        double totalAmount = amount + commission;

        if (account1.getBalance() < totalAmount) {
            return new OperationResult.OperationError("Insufficient funds, including commission.");
        }

        _bankAccountRepository.UpdateBankAccountBalance(account1.getId(), account1.getBalance() - totalAmount);
        _bankAccountRepository.UpdateBankAccountBalance(account2.getId(), account2.getBalance() + amount);

        _operationRepository.AddOperation(new Operation(bankAccount1.getId(), OperationType.Transfer, totalAmount));
        _operationRepository.AddOperation(new Operation(bankAccount2.getId(), OperationType.Deposit, amount));

        return new OperationResult.Success();
    }

    /**
     * Получает историю операций по банковскому счету.
     *
     * @param bankAccount объект банковского счета, для которого нужно получить историю операций.
     * @return result-type с выводом историей операций в консоль.
     */
    @Override
    public OperationResult GetOperationHistory(BankAccount bankAccount) {
        var account = _bankAccountRepository.FindBankAccountById(bankAccount.getId());
        var operations = _operationRepository.GetOperationHistory(account.getId());
        return _userManager.PrintHistory(account, operations);
    }
}
