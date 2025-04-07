package Application.Managers;

import Application.Models.Entities.BankAccount;
import Application.Models.Entities.Operation;
import Application.Models.Entities.User;
import Application.ResultTypes.OperationResult;

import java.util.List;

public interface IUserManager {
    void GetUserInfo(User user);

    void AddFriend(User user, User other);

    void RemoveFriend(User user, User other);

    void AddBankAccount(User user, BankAccount bankAccount);

    void RemoveBankAccount(User user, BankAccount bankAccount);

    void CheckBalance(User user, BankAccount bankAccount);

    OperationResult PrintHistory(BankAccount account, List<Operation> operations);
}
