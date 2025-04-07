package Application.Managers;

import Application.ResultTypes.OperationResult;
import Application.Models.Entities.BankAccount;
import Application.Models.Entities.Operation;
import Application.Models.Entities.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserManager implements IUserManager {

    public void GetUserInfo(User user) {

        if (user == null) {
            System.out.println("Пользователь с ID: [null] не найден.");
            return;
        }

        System.out.println("ID: " + user.getId());
        System.out.println("Логин: " + user.getLogin());
        System.out.println("Имя: " + user.getName());
        System.out.println("Возраст: " + user.getAge());
        System.out.println("Пол: " + user.getSex());
        System.out.println("Цвет волос: " + user.getHairType());

        System.out.println("\nБанковские счета:");
        if (user.getBankAccounts().isEmpty()) {
            System.out.println("Нет привязанных счетов.");
        } else {
            for (BankAccount account : user.getBankAccounts()) {
                System.out.println("ID счета: " + account.getId() + ", Баланс: " + account.getBalance());
            }
        }

        System.out.println("\nДрузья:");
        if (user.getFriends().isEmpty()) {
            System.out.println("Нет друзей.");
        } else {
            for (User friend : user.getFriends()) {
                System.out.println("ID друга: " + friend.getId() + ", Имя: " + friend.getName());
            }
        }
    }

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
    }

    public void RemoveBankAccount(User user, BankAccount bankAccount) {
        user.getBankAccounts().remove(bankAccount.getId());
    }

    public void CheckBalance(User user, BankAccount bankAccount) {
        System.out.println("User: " + user.getId());
        System.out.println("BankAccount: " + bankAccount.getId());
        System.out.println("Balance: " + bankAccount.getBalance());
    }

    public OperationResult PrintHistory(BankAccount account, List<Operation> operations) {
        if (operations == null) {
            return new OperationResult.OperationError("Operations can not be null.");
        } else {
            System.out.println("Account Id: " + account.getId());
            for (Operation operation : operations) {
                System.out.println("Operation: " + operation.getType() + "Amount: " + operation.getAmount());
            }
            return new OperationResult.Success();
        }
    }
}
