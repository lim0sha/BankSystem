package Application.Contracts.Interfaces;

import Application.Contracts.ResultTypes.BankAccountResult;
import Application.Contracts.ResultTypes.OperationResult;
import Application.Contracts.ResultTypes.UserResult;
import Application.Models.Entites.BankAccount;
import Application.Models.Entites.User;

public interface IUserService {
    UserResult CreateUser(User user);

    UserResult DeleteUser(User user);

    void GetUserInfo(User user);

    void AddFriend(User user, User other);

    void RemoveFriend(User user, User other);

    BankAccountResult addBankAccount(User user, BankAccount bankAccount);

    BankAccountResult RemoveBankAccount(User user, BankAccount bankAccount);

    OperationResult CheckBalance(User user, BankAccount bankAccount);

    OperationResult Deposit(BankAccount bankAccount, Double amount);

    OperationResult Withdraw(BankAccount bankAccount, Double amount);

    OperationResult Transfer(BankAccount bankAccount1, BankAccount bankAccount2, Double amount);

    OperationResult GetOperationHistory(BankAccount bankAccount);
}
