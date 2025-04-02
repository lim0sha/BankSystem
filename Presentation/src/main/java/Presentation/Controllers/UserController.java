package Presentation.Controllers;

import Application.Managers.UserManager;
import Application.Models.Entities.BankAccount;
import Application.Models.Entities.Operation;
import Application.Models.Entities.User;
import Application.Models.Enums.OperationType;
import Application.ResultTypes.BankAccountResult;
import Application.ResultTypes.OperationResult;
import Application.ResultTypes.UserResult;
import Presentation.Interfaces.IUserController;
import Services.BankAccountService;
import Services.OperationService;
import Services.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserController implements IUserController {
    private final UserManager UserManager;
    private final UserService UserService;
    private final BankAccountService BankAccountService;
    private final OperationService OperationService;

    public UserController(UserManager userManager, UserService userService, BankAccountService bankAccountService, OperationService operationService) {
        UserManager = userManager;
        UserService = userService;
        BankAccountService = bankAccountService;
        OperationService = operationService;
    }

    @Override
    public UserResult CreateUser(User user) {
        if (user == null || user.getLogin() == null || user.getName() == null) {
            return new UserResult.UserCreationError("Некорректные данные пользователя");
        }
        UserService.SaveUser(user);
        return new UserResult.Success();
    }

    @Override
    public User GetUserById(int id) {
        try {
            return UserService.GetUser(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public BankAccount GetBankAccountById(int id) {
        try {
            return BankAccountService.GetAccount(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserResult DeleteUser(User user) {
        try {
            UserService.DeleteUser(user);
            return new UserResult.Success();
        } catch (Exception e) {
            return new UserResult.UserDeletionError(e.getMessage());
        }
    }

    @Override
    public void GetUserInfo(int id) {
        UserManager.GetUserInfo(UserService.GetUser(id));
    }

    @Override
    public void AddFriend(int userId, int otherId) {
        User user1 = UserService.GetUser(userId);
        User user2 = UserService.GetUser(otherId);
        UserManager.AddFriend(user1, user2);
        UserService.SaveUser(user1);
        UserService.SaveUser(user2);
    }

    @Override
    public void RemoveFriend(int userId, int otherId) {
        User user1 = UserService.GetUser(userId);
        User user2 = UserService.GetUser(otherId);
        UserManager.RemoveFriend(user1, user2);
        UserService.SaveUser(user1);
        UserService.SaveUser(user2);
    }

    @Override
    public BankAccountResult addBankAccount(int userId, BankAccount bankAccount) {
        try {
            User user = UserService.GetUser(userId);
            UserManager.AddBankAccount(user, bankAccount);
            UserService.SaveUser(user);
            BankAccountService.UpdateAccount(bankAccount);
            return new BankAccountResult.Success();
        } catch (Exception e) {
            return new BankAccountResult.BankAccountCreationError(e.getMessage());
        }
    }

    @Override
    public BankAccountResult RemoveBankAccount(int userId, BankAccount bankAccount) {
        try {
            User user = UserService.GetUser(userId);
            UserManager.RemoveBankAccount(user, bankAccount);
            UserService.SaveUser(user);
            BankAccountService.UpdateAccount(bankAccount);
            return new BankAccountResult.Success();
        } catch (Exception e) {
            return new BankAccountResult.BankAccountDeletionError(e.getMessage());
        }
    }

    @Override
    public OperationResult CheckBalance(int userId, int accountId) {
        BankAccount account = BankAccountService.GetAccount(accountId);
        if (account == null || account.getUser().getId() != userId) {
            return new OperationResult.OperationError("Счет не найден или принадлежит другому пользователю.");
        }
        System.out.println("Баланс счета: " + account.getBalance());
        return new OperationResult.Success();
    }

    @Override
    public OperationResult Deposit(BankAccount bankAccount, Double amount) {
        try {
            BankAccountService.Deposit(bankAccount.getId(), amount);
            OperationService.SaveOperation(new Operation(bankAccount, OperationType.Deposit, amount));
            return new OperationResult.Success();
        } catch (Exception e) {
            return new OperationResult.OperationError(e.getMessage());
        }
    }

    @Override
    public OperationResult Withdraw(BankAccount bankAccount, Double amount) {
        try {
            boolean success = BankAccountService.Withdraw(bankAccount.getId(), amount);
            if (!success) {
                return new OperationResult.OperationError("Not enough balance.");
            }
            OperationService.SaveOperation(new Operation(bankAccount, OperationType.Withdraw, amount));
            return new OperationResult.Success();
        } catch (Exception e) {
            return new OperationResult.OperationError(e.getMessage());
        }
    }

    @Override
    public OperationResult Transfer(BankAccount bankAccount1, BankAccount bankAccount2, Double amount) {
        try {
            User user1 = UserService.GetUser(bankAccount1.getUser().getId());
            User user2 = UserService.GetUser(bankAccount2.getUser().getId());

            double commissionRate = user1.getId().equals(user2.getId()) ? 0.00
                    : user1.getFriends().stream().anyMatch(f -> f.getId().equals(user2.getId())) ? 0.03
                    : 0.10;
            double commission = amount * commissionRate;
            double totalAmount = amount + commission;

            boolean success = BankAccountService.Transfer(bankAccount1.getId(), bankAccount2.getId(), totalAmount);
            if (!success) {
                return new OperationResult.OperationError("Insufficient funds, including commission.");
            }

            OperationService.SaveOperation(new Operation(bankAccount1, OperationType.Transfer, totalAmount));
            OperationService.SaveOperation(new Operation(bankAccount2, OperationType.Deposit, amount));

            return new OperationResult.Success();
        } catch (Exception e) {
            return new OperationResult.OperationError(e.getMessage());
        }
    }

    @Override
    public OperationResult GetOperationHistory(int bankAccountId) {
        try {
            BankAccount account = BankAccountService.GetAccount(bankAccountId);
            List<Operation> operations = OperationService.FindAllOperationsByAccountId(account.getId());
            return UserManager.PrintHistory(account, operations);
        } catch (Exception e) {
            e.printStackTrace();
            return new OperationResult.OperationError(e.getMessage());
        }
    }

}
