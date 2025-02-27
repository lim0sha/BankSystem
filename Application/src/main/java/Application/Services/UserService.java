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

@AllArgsConstructor
public class UserService implements IUserService {

    @Getter
    private IUserRepository _userRepository;
    @Getter
    private IBankAccountRepository _bankAccountRepository;
    @Getter
    private IOperationRepository _operationRepository;

    private UserManager _userManager;

    @Override
    public UserResult CreateUser(User user) {
        return _userRepository.AddUser(user);
    }

    @Override
    public UserResult DeleteUser(User user) {
        return _userRepository.DeleteUser(user.getId());
    }

    @Override
    public void GetUserInfo(User user) {
        _userManager.GetUserInfo(user);
    }

    @Override
    public void AddFriend(User user, User other) {
        _userManager.AddFriend(user, other);
    }

    @Override
    public void RemoveFriend(User user, User other) {
        _userManager.RemoveFriend(user, other);
    }

    @Override
    public BankAccountResult addBankAccount(User user, BankAccount bankAccount) {
        _userManager.AddBankAccount(user, bankAccount);
        return _bankAccountRepository.AddBankAccount(bankAccount);
    }

    @Override
    public BankAccountResult RemoveBankAccount(User user, BankAccount bankAccount) {
        _userManager.RemoveBankAccount(user, bankAccount);
        return _bankAccountRepository.DeleteBankAccount(bankAccount.getId());
    }

    @Override
    public OperationResult CheckBalance(User user, BankAccount bankAccount) {
        _userManager.CheckBalance(user, bankAccount);
        return _operationRepository.AddOperation(new Operation(bankAccount.getId(), OperationType.CheckBalance, 0.0));
    }

    @Override
    public OperationResult Deposit(BankAccount bankAccount, Double amount) {
        var account = _bankAccountRepository.FindBankAccountById(bankAccount.getId());
        _bankAccountRepository.UpdateBankAccountBalance(account.getId(), account.getBalance() + amount);
        return _operationRepository.AddOperation(new Operation(bankAccount.getId(), OperationType.Deposit, amount));
    }

    @Override
    public OperationResult Withdraw(BankAccount bankAccount, Double amount) {
        var account = _bankAccountRepository.FindBankAccountById(bankAccount.getId());
        if (account.getBalance() - amount >= 0) {
            _bankAccountRepository.UpdateBankAccountBalance(account.getId(), account.getBalance() - amount);
            return _operationRepository.AddOperation(new Operation(bankAccount.getId(), OperationType.Withdraw, amount));
        }
        return new OperationResult.OperationError("Not enough balance");
    }

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

    @Override
    public OperationResult GetOperationHistory(BankAccount bankAccount) {
        var account = _bankAccountRepository.FindBankAccountById(bankAccount.getId());
        var operations = _operationRepository.GetOperationHistory(account.getId());
        return _userManager.PrintHistory(account, operations);
    }
}
