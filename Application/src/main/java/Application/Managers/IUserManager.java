package Application.Managers;

import Application.Models.Entities.BankAccount;
import Application.Models.Entities.User;

public interface IUserManager {

    void AddFriend(User user, User other);

    void RemoveFriend(User user, User other);

    void AddBankAccount(User user, BankAccount bankAccount);

    void RemoveBankAccount(User user, BankAccount bankAccount);
}
