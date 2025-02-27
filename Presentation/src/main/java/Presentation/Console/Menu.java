package Presentation.Console;

import Application.Contracts.ResultTypes.BankAccountResult;
import Application.Contracts.ResultTypes.OperationResult;
import Application.Contracts.ResultTypes.UserResult;
import Application.Managers.UserManager;
import Application.Models.Entites.BankAccount;
import Application.Models.Entites.User;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.Sex;
import Application.Models.Utils.IdGenerator;
import Application.Services.UserService;
import Presentation.Interfaces.IMenu;
import DataAccess.BankAccountRepository;
import DataAccess.OperationRepository;
import DataAccess.UserRepository;

import java.util.Scanner;

public class Menu implements IMenu {

    private final IdGenerator userIdGenerator = new IdGenerator();
    private final IdGenerator bankAccountIdGenerator = new IdGenerator();
    private final UserService userService = new UserService(
            new UserRepository(),
            new BankAccountRepository(),
            new OperationRepository(),
            new UserManager());

    private final Scanner scanner = new Scanner(System.in);

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
        String login = scanner.nextLine();
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите возраст: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        // Ввод пола через выбор цифрой
        System.out.println("Выберите пол:");
        System.out.println("1. Мужской");
        System.out.println("2. Женский");
        System.out.println("3. Неопределённый");
        System.out.print("Введите цифру (1-3): ");
        int sexChoice = scanner.nextInt();
        scanner.nextLine();

        Sex sex = switch (sexChoice) {
            case 1 -> Sex.Male;
            case 2 -> Sex.Female;
            default -> Sex.Undefined;
        };

        // Ввод цвета волос через выбор цифрой
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
        int hairChoice = scanner.nextInt();
        scanner.nextLine();

        HairColor hairColor = switch (hairChoice) {
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

        User user = new User(userIdGenerator, login, name, age, sex, hairColor);
        UserResult result = userService.CreateUser(user);

        System.out.println(result instanceof UserResult.Success ? "Пользователь создан!" : "Ошибка создания пользователя.");
    }


    private void getUserInfo() {
        System.out.print("Введите ID пользователя: ");
        int userId = scanner.nextInt();
        User user = userService.get_userRepository().FindUserById(userId);
        if (user != null) {
            userService.GetUserInfo(user);
        } else {
            System.out.println("Пользователь не найден.");
        }
    }

    private void manageFriends() {
        System.out.print("Введите ID пользователя: ");
        int userId = scanner.nextInt();
        System.out.print("Введите ID друга: ");
        int friendId = scanner.nextInt();

        User user = userService.get_userRepository().FindUserById(userId);
        User friend = userService.get_userRepository().FindUserById(friendId);

        if (user != null && friend != null) {
            System.out.print("Добавить друга (1) или удалить (2): ");
            int action = scanner.nextInt();

            if (action == 1) {
                userService.AddFriend(user, friend);
                System.out.println("Друг добавлен.");
            } else if (action == 2) {
                userService.RemoveFriend(user, friend);
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

        User user = userService.get_userRepository().FindUserById(userId);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return; // 💥 Выход, если пользователь не найден
        }

        System.out.println("Найден пользователь: " + user.getName() + " (ID: " + user.getId() + ")");

        BankAccount account = new BankAccount(bankAccountIdGenerator, user);
        BankAccountResult result = userService.addBankAccount(user, account);

        if (result instanceof BankAccountResult.Success) {
            System.out.println("Счет создан!");
        } else {
            System.out.println("Ошибка создания счета.");
        }
    }


    private void checkBalance() {
        System.out.print("Введите ID счета: ");
        int accountId = scanner.nextInt();
        BankAccount account = userService.get_bankAccountRepository().FindBankAccountById(accountId);

        if (account != null) {
            User user = userService.get_userRepository().FindUserById(account.getUserId()); // ✅ Получаем пользователя из счета
            if (user != null) {
                userService.CheckBalance(user, account); // ✅ Передаём пользователя
            } else {
                System.out.println("Пользователь не найден.");
            }
        } else {
            System.out.println("Счет не найден.");
        }
    }


    private void withdraw() {
        System.out.print("Введите ID счета: ");
        int accountId = scanner.nextInt();
        System.out.print("Введите сумму снятия: ");
        double amount = scanner.nextDouble();

        BankAccount account = userService.get_bankAccountRepository().FindBankAccountById(accountId);
        if (account != null) {
            OperationResult result = userService.Withdraw(account, amount);

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

        BankAccount account = userService.get_bankAccountRepository().FindBankAccountById(accountId);
        if (account != null) {
            OperationResult result = userService.Deposit(account, amount);
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

            BankAccount fromAccount = userService.get_bankAccountRepository().FindBankAccountById(fromId);
            BankAccount toAccount = userService.get_bankAccountRepository().FindBankAccountById(toId);

            if (fromAccount != null && toAccount != null) {
                OperationResult result = userService.Transfer(fromAccount, toAccount, amount);
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
