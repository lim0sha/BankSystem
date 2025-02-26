package Application.Managers;

import Application.Contracts.ResultTypes.OperationResult;
import Application.Models.Entites.BankAccount;
import Application.Models.Entites.Operation;
import Application.Models.Entites.User;

import java.util.ArrayList;

public record UserManager() {

    public void GetUserInfo(User user) {
        if (user == null) {
            System.out.println("User data is not available.");
            return;
        }

        System.out.println("User Information:");
        System.out.println("──────────────────────────────────");
        System.out.printf("ID: %d%n", user.getId());
        System.out.printf("Login: %s%n", user.getLogin());
        System.out.printf("Name: %s%n", user.getName());
        System.out.printf("Age: %d%n", user.getAge());
        System.out.printf("Sex: %s%n", user.getSex());
        System.out.printf("Hair Color: %s%n", user.getHairType());

        System.out.println("\nBank Accounts:");
        if (user.getBankAccounts().isEmpty()) {
            System.out.println("No bank accounts available.");
        } else {
            user.getBankAccounts().forEach(account -> System.out.printf("Account ID: %d%n", account));
        }

        System.out.println("\nFriends:");
        if (user.getFriends().isEmpty()) {
            System.out.println("No friends available.");
        } else {
            user.getFriends().forEach(friend ->
                    System.out.printf("Friend ID: %d | Name: %s%n", friend.getId(), friend.getName()));
        }

        System.out.println("──────────────────────────────────\n");
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
        user.getBankAccounts().add(bankAccount.getId());
    }

    public void RemoveBankAccount(User user, BankAccount bankAccount) {
        user.getBankAccounts().remove(bankAccount.getId());
    }

    public void CheckBalance(User user, BankAccount bankAccount) {
        System.out.println("User: " + user.getId());
        System.out.println("BankAccount: " + bankAccount.getId());
        System.out.println("Balance: " + bankAccount.getBalance());
    }

    public OperationResult PrintHistory(BankAccount account, ArrayList<Operation> operations) {
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
