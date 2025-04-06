package Presentation.Console;

import Application.ResultTypes.BankAccountResult;
import Application.ResultTypes.OperationResult;
import Application.ResultTypes.UserResult;
import Application.Models.Entities.BankAccount;
import Application.Models.Entities.User;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.Sex;
import Presentation.Controllers.UserController;
import Presentation.Interfaces.IMenu;
import Presentation.Interfaces.IUserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@ComponentScan(basePackages = "Presentation")
public class Menu implements IMenu {

    private final IUserController controller;
    private final Scanner scanner;

    @Autowired
    public Menu(UserController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    @Override
    public void Run() {
        while (true) {
            System.out.println("\n--- Меню ---");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Показать информацию о пользователе");
            System.out.println("3. Изменить список друзей пользователя");
            System.out.println("4. Создать счет пользователя");
            System.out.println("5. Просмотреть баланс счета");
            System.out.println("6. Снять деньги со счета");
            System.out.println("7. Пополнить счет");
            System.out.println("8. Перевести деньги");
            System.out.println("0. Выйти");
            System.out.print("Выберите команду: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createUser();
                case 2 -> getUserInfo();
                case 3 -> manageFriends();
                case 4 -> createBankAccount();
                case 5 -> checkBalance();
                case 6 -> withdraw();
                case 7 -> deposit();
                case 8 -> transfer();
                case 0 -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверная команда!");
            }
        }
    }

    private void createUser() {
        System.out.print("Введите логин: ");
        String login = scanner.nextLine().trim();
        if (login.isEmpty()) {
            System.out.println("Логин не может быть пустым!");
            return;
        }

        System.out.print("Введите имя: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Имя не может быть пустым!");
            return;
        }

        System.out.print("Введите возраст: ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine().trim());
            if (age <= 0) {
                System.out.println("Возраст должен быть положительным числом!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат возраста. Введите число.");
            return;
        }

        System.out.println("Выберите пол:");
        System.out.println("1. Мужской");
        System.out.println("2. Женский");
        System.out.println("3. Неопределённый");
        System.out.print("Введите цифру (1-3): ");
        Sex sex;
        try {
            int sexChoice = Integer.parseInt(scanner.nextLine().trim());
            sex = switch (sexChoice) {
                case 1 -> Sex.Male;
                case 2 -> Sex.Female;
                default -> Sex.Undefined;
            };
        } catch (NumberFormatException e) {
            System.out.println("Неверный выбор пола. Введите число от 1 до 3.");
            return;
        }

        System.out.println("Выберите цвет волос:");
        System.out.println("1. Блонд");
        System.out.println("2. Пепельный");
        System.out.println("3. Каштановый");
        System.out.println("4. Рыжий");
        System.out.println("5. Черный");
        System.out.println("6. Окрашенный");
        System.out.println("7. Цветной");
        System.out.println("8. Седой");
        System.out.println("9. Белый");
        System.out.println("10. Лысый");
        System.out.print("Введите цифру (1-10): ");
        HairColor hairColor;
        try {
            int hairChoice = Integer.parseInt(scanner.nextLine().trim());
            hairColor = switch (hairChoice) {
                case 1 -> HairColor.Blond;
                case 2 -> HairColor.Ash;
                case 3 -> HairColor.Brown;
                case 4 -> HairColor.Auburn;
                case 5 -> HairColor.Black;
                case 6 -> HairColor.Dyed;
                case 7 -> HairColor.Colored;
                case 8 -> HairColor.Grey;
                case 9 -> HairColor.White;
                default -> HairColor.Bold;
            };
        } catch (NumberFormatException e) {
            System.out.println("Неверный выбор цвета волос. Введите число от 1 до 10.");
            return;
        }

        User user = new User(login, name, age, sex, hairColor);

        UserResult result = controller.CreateUser(user);
        if (result instanceof UserResult.Success) {
            System.out.println("Пользователь создан!");
        } else {
            System.out.println("Ошибка создания пользователя.");
        }
    }

    private void getUserInfo() {
        System.out.print("Введите ID пользователя: ");
        int userId = scanner.nextInt();
        controller.GetUserInfo(userId);
    }

    private void manageFriends() {
        System.out.print("Введите ID пользователя: ");
        int userId = scanner.nextInt();
        System.out.print("Введите ID друга: ");
        int friendId = scanner.nextInt();
        if (userId > 0 && friendId > 0) {
            System.out.print("Добавить друга (1) или удалить (2): ");
            int action = scanner.nextInt();

            if (action == 1) {
                controller.AddFriend(userId, friendId);
                System.out.println("Друг добавлен.");
            } else if (action == 2) {
                controller.RemoveFriend(userId, friendId);
                System.out.println("Друг удален.");
            } else {
                System.out.println("Неверная команда.");
            }
        } else {
            System.out.println("Пользователь или друг не найден.");
        }
    }

    private void createBankAccount() {
        System.out.print("Введите ID пользователя: ");
        int userId = scanner.nextInt();
        User user = controller.GetUserById(userId);
        if (user == null) {
            System.out.println("Пользователь с ID " + userId + " не найден.");
            return;
        }

        BankAccount account = new BankAccount(user);
        BankAccountResult result = controller.addBankAccount(userId, account);
        if (result instanceof BankAccountResult.Success) {
            System.out.println("Счет создан!");
        } else {
            System.out.println("Ошибка создания счета.");
        }
    }

    private void checkBalance() {
        System.out.print("Введите ID счета: ");
        int accountId = scanner.nextInt();
        BankAccount account = controller.GetBankAccountById(accountId);

        if (account == null) {
            System.out.println("Счет не найден.");
            return;
        }

        controller.CheckBalance(account.getUser().getId(), accountId);
    }

    private void withdraw() {
        System.out.print("Введите ID счета: ");
        int accountId = scanner.nextInt();
        System.out.print("Введите сумму снятия: ");
        double amount = scanner.nextDouble();

        BankAccount account = controller.GetBankAccountById(accountId);
        if (account != null) {
            OperationResult result = controller.Withdraw(account, amount);

            if (result instanceof OperationResult.Success) {
                System.out.println("Деньги сняты.");
            } else if (result instanceof OperationResult.OperationError error) {
                System.out.println("Ошибка: " + error.getMessage());
            }
        } else {
            System.out.println("Счет не найден.");
        }
    }

    private void deposit() {
        System.out.print("Введите ID счета: ");
        int accountId = scanner.nextInt();
        System.out.print("Введите сумму пополнения: ");
        double amount = scanner.nextDouble();

        BankAccount account = controller.GetBankAccountById(accountId);
        if (account != null) {
            OperationResult result = controller.Deposit(account, amount);
            if (result instanceof OperationResult.Success) {
                System.out.println("Счет пополнен.");
            } else if (result instanceof OperationResult.OperationError error) {
                System.out.println("Ошибка: " + error.getMessage());
            }
        } else {
            System.out.println("Счет не найден.");
        }
    }

    private void transfer() {
        try {
            System.out.print("Введите ID счета отправителя: ");
            int fromId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Введите ID счета получателя: ");
            int toId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Введите сумму перевода: ");
            double amount = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));

            BankAccount fromAccount = controller.GetBankAccountById(fromId);
            BankAccount toAccount = controller.GetBankAccountById(toId);

            if (fromAccount != null && toAccount != null) {
                OperationResult result = controller.Transfer(fromAccount, toAccount, amount);
                if (result instanceof OperationResult.Success) {
                    System.out.println("Перевод выполнен.");
                } else if (result instanceof OperationResult.OperationError error) {
                    System.out.println("Ошибка: " + error.getMessage());
                }
            } else {
                System.out.println("Один или оба счета не найдены.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка ввода! Пожалуйста, введите корректные числовые значения.");
        }
    }
}
