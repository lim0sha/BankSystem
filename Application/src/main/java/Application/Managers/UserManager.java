package Application.Managers;

import Application.Models.Entities.BankAccount;
import Application.Models.Entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserManager implements IUserManager {

    public void AddFriend(User user, User other) {
        user.getFriends().add(other);
        other.getFriends().add(user);
    }

    public void RemoveFriend(User user, User other) {
        user.getFriends().remove(other);
        other.getFriends().remove(user);
    }

    public void AddBankAccount(User user, BankAccount bankAccount) {
        user.getBankAccounts().add(bankAccount);
        bankAccount.setUser(user);
    }

    public void RemoveBankAccount(User user, BankAccount bankAccount) {
        user.getBankAccounts().remove(bankAccount.getId());
    }
}
