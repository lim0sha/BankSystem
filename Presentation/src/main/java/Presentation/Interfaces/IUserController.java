package Presentation.Interfaces;

import Application.ResultTypes.BankAccountResult;
import Application.ResultTypes.OperationResult;
import Application.ResultTypes.UserResult;
import Application.Models.Entities.BankAccount;
import Application.Models.Entities.User;

public interface IUserController {

    UserResult CreateUser(User user);

    UserResult UpdateUser(User user);

    User GetUserById(int id);

    BankAccount GetBankAccountById(int id);

    UserResult DeleteUser(User user);

    void GetUserInfo(int id);

    void AddFriend(int userId, int otherId);

    void RemoveFriend(int userId, int otherId);

    BankAccountResult addBankAccount(int userId, BankAccount bankAccount);

    BankAccountResult RemoveBankAccount(int userId, BankAccount bankAccount);

    OperationResult CheckBalance(int userId, int bankAccountId);

    OperationResult Deposit(BankAccount bankAccount, Double amount);

    OperationResult Withdraw(BankAccount bankAccount, Double amount);

    OperationResult Transfer(BankAccount bankAccount1, BankAccount bankAccount2, Double amount);

    OperationResult GetOperationHistory(int bankAccountId);

    UserResult DeleteUserById(int id);
}
