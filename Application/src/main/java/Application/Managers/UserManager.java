package Application.Managers;

import Application.Contracts.ResultTypes.OperationResult;
import Application.Models.Entites.BankAccount;
import Application.Models.Entites.Operation;
import Application.Models.Entites.User;

import java.util.ArrayList;

/**
 * Класс-менеджер для управления данными пользователей, их банковскими счетами и друзьями.
 * Предоставляет методы для получения информации о пользователе, добавления/удаления друзей,
 * управления банковскими счетами и вывода истории операций.
 */
public record UserManager() {

    /**
     * Выводит информацию о пользователе, включая его данные и банковские счета.
     *
     * @param user объект пользователя, информацию о котором нужно вывести.
     */
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

    /**
     * Добавляет друга пользователю и другому пользователю.
     *
     * @param user текущий пользователь, добавляющий друга.
     * @param other пользователь, который будет добавлен в друзья.
     */
    public void AddFriend(User user, User other) {
        user.getFriends().add(other);
        other.getFriends().add(user);
    }

    /**
     * Удаляет друга у пользователя и другого пользователя.
     *
     * @param user текущий пользователь, у которого будет удален друг.
     * @param other пользователь, который будет удален из друзей.
     */
    public void RemoveFriend(User user, User other) {
        user.getFriends().remove(other);
        other.getFriends().remove(user);
    }

    /**
     * Добавляет банковский счет пользователю.
     *
     * @param user пользователь, которому добавляется банковский счет.
     * @param bankAccount банковский счет для добавления.
     */
    public void AddBankAccount(User user, BankAccount bankAccount) {
        user.getBankAccounts().add(bankAccount.getId());
    }

    /**
     * Удаляет банковский счет у пользователя.
     *
     * @param user пользователь, у которого будет удален банковский счет.
     * @param bankAccount банковский счет для удаления.
     */
    public void RemoveBankAccount(User user, BankAccount bankAccount) {
        user.getBankAccounts().remove(bankAccount.getId());
    }

    /**
     * Проверяет баланс банковского счета пользователя.
     *
     * @param user пользователь, чей баланс проверяется.
     * @param bankAccount банковский счет для проверки баланса.
     */
    public void CheckBalance(User user, BankAccount bankAccount) {
        System.out.println("User: " + user.getId());
        System.out.println("BankAccount: " + bankAccount.getId());
        System.out.println("Balance: " + bankAccount.getBalance());
    }

    /**
     * Выводит историю операций по банковскому счету.
     *
     * @param account банковский счет, для которого нужно вывести историю.
     * @param operations список операций, связанных с этим счетом.
     * @return результат операции (успех или ошибка).
     */
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
