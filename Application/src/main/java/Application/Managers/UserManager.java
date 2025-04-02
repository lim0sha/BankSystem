package Application.Managers;

import Application.ResultTypes.OperationResult;
import Application.Models.Entities.BankAccount;
import Application.Models.Entities.Operation;
import Application.Models.Entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс-менеджер для управления данными пользователей, их банковскими счетами и друзьями.
 * Предоставляет методы для получения информации о пользователе, добавления/удаления друзей,
 * управления банковскими счетами и вывода истории операций.
 */
public class UserManager {

    /**
     * Выводит информацию о пользователе, включая его данные и банковские счета.
     *
     * @param user объект пользователя, информацию о котором нужно вывести.
     */
    public void GetUserInfo(User user) {

        if (user == null) {
            System.out.println("Пользователь с ID " + user.getId() + " не найден.");
            return;
        }

//        // Инициализация ленивых коллекций
//        Hibernate.initialize(user.getBankAccounts());
//        Hibernate.initialize(user.getFriends());

        System.out.println("ID: " + user.getId());
        System.out.println("Логин: " + user.getLogin());
        System.out.println("Имя: " + user.getName());
        System.out.println("Возраст: " + user.getAge());
        System.out.println("Пол: " + user.getSex());
        System.out.println("Цвет волос: " + user.getHairType());

        // Вывод банковских счетов
        System.out.println("\nБанковские счета:");
        if (user.getBankAccounts().isEmpty()) {
            System.out.println("Нет привязанных счетов.");
        } else {
            for (BankAccount account : user.getBankAccounts()) {
                System.out.println("ID счета: " + account.getId() + ", Баланс: " + account.getBalance());
            }
        }

        // Вывод друзей
        System.out.println("\nДрузья:");
        if (user.getFriends().isEmpty()) {
            System.out.println("Нет друзей.");
        } else {
            for (User friend : user.getFriends()) {
                System.out.println("ID друга: " + friend.getId() + ", Имя: " + friend.getName());
            }
        }
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
        user.getBankAccounts().add(bankAccount);
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
