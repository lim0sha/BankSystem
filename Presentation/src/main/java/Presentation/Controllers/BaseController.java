package Presentation.Controllers;

import Application.Managers.IUserManager;
import Application.Models.Entities.BankAccount;
import Application.Models.Entities.Operation;
import Application.Models.Entities.User;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.OperationType;
import Application.Models.Enums.Sex;
import Application.ResultTypes.BankAccountResult;
import Application.ResultTypes.OperationResult;
import Application.ResultTypes.UserResult;
import Presentation.Interfaces.IBaseController;
import DataAccess.Services.Interfaces.IBankAccountService;
import DataAccess.Services.Interfaces.IOperationService;
import DataAccess.Services.Interfaces.IUserService;
import Presentation.Kafka.Services.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaseController implements IBaseController {

    private final IUserManager userManager;
    private final IUserService userService;
    private final IBankAccountService bankAccountService;
    private final IOperationService operationService;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public BaseController(IUserManager userManager, IUserService userService, IBankAccountService bankAccountService, IOperationService operationService, KafkaProducerService kafkaProducerService) {
        this.userManager = userManager;
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.operationService = operationService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public UserResult CreateUser(User user) {
        try {
            if (user == null || user.getLogin() == null || user.getName() == null) {
                return new UserResult.UserCreationError("Некорректные данные пользователя");
            }
            userService.SaveUser(user);
            kafkaProducerService.sendEvent("client-topic", String.valueOf(user.getId()), user);
            return new UserResult.Success();
        } catch (Exception e) {
            return new UserResult.UserCreationError(e.getMessage());
        }
    }

    @Override
    public UserResult UpdateUser(User user) {
        try {
            userService.SaveUser(user);
            kafkaProducerService.sendEvent("client-topic", String.valueOf(user.getId()), user);
            return new UserResult.Success();
        } catch (Exception e) {
            return new UserResult.UserUpdateError(e.getMessage());
        }
    }

    @Override
    public User GetUserById(int id) {
        try {
            return userService.GetUser(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<User> GetAllUsersFiltered(Sex sex, HairColor color) {
        List<User> users = userService.GetAllUsers();
        return users.stream()
                .filter(u -> (sex == null || u.getSex() == sex))
                .filter(u -> (color == null || u.getHairType() == color))
                .toList();
    }

    @Override
    public List<User> GetFriends(int userId) {
        User user = userService.GetUser(userId);
        if (user != null) {
            return user.getFriends();
        }
        return List.of();
    }

    @Override
    public List<BankAccount> GetUserBankAccounts(int userId) {
        User user = userService.GetUser(userId);
        if (user != null) {
            return user.getBankAccounts();
        }
        return List.of();
    }

    @Override
    public List<BankAccount> GetAllAccounts() {
        return bankAccountService.GetAllAccounts();
    }

    @Override
    public List<Operation> GetFilteredOperations(OperationType type, Integer accountId) {
        List<Operation> operations;

        if (accountId != null) {
            operations = operationService.FindAllOperationsByAccountId(accountId);
        } else {
            operations = operationService.FindAllOperations();
        }

        if (type != null) {
            operations = operations.stream()
                    .filter(o -> o.getType().equalsIgnoreCase(type.toString()))
                    .toList();
        }

        return operations;
    }


    @Override
    public BankAccount GetBankAccountById(int id) {
        try {
            return bankAccountService.GetAccount(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserResult DeleteUser(User user) {
        try {
            userService.DeleteUser(user);
            return new UserResult.Success();
        } catch (Exception e) {
            return new UserResult.UserDeletionError(e.getMessage());
        }
    }

    @Override
    public void AddFriend(int userId, int otherId) {
        User user1 = userService.GetUser(userId);
        User user2 = userService.GetUser(otherId);
        userManager.AddFriend(user1, user2);
        userService.SaveUser(user1);
        userService.SaveUser(user2);
    }

    @Override
    public void RemoveFriend(int userId, int otherId) {
        User user1 = userService.GetUser(userId);
        User user2 = userService.GetUser(otherId);
        userManager.RemoveFriend(user1, user2);
        userService.SaveUser(user1);
        userService.SaveUser(user2);
    }

    @Override
    public BankAccountResult AddBankAccount(int userId, BankAccount bankAccount) {
        try {
            User user = userService.GetUser(userId);
            if (user == null) {
                throw new IllegalStateException("User not found");
            }
            bankAccount.setId(null);
            userManager.AddBankAccount(user, bankAccount);
            userService.SaveUser(user);
            kafkaProducerService.sendEvent("account-topic", String.valueOf(bankAccount.getId()), bankAccount);
            return new BankAccountResult.Success();
        } catch (Exception e) {
            return new BankAccountResult.BankAccountCreationError(e.getMessage());
        }
    }

    @Override
    public BankAccountResult UpdateBankAccount(BankAccount bankAccount) {
        try {
            if (bankAccount == null || bankAccount.getId() == null) {
                return new BankAccountResult.BankAccountUpdateError("Некорректные данные счета");
            }
            bankAccountService.UpdateAccount(bankAccount);
            kafkaProducerService.sendEvent("account-topic", String.valueOf(bankAccount.getId()), bankAccount);
            return new BankAccountResult.Success();
        } catch (Exception e) {
            return new BankAccountResult.BankAccountUpdateError(e.getMessage());
        }
    }

    @Override
    public BankAccountResult RemoveBankAccount(int userId, BankAccount bankAccount) {
        try {
            User user = userService.GetUser(userId);
            userManager.RemoveBankAccount(user, bankAccount);
            userService.SaveUser(user);
            bankAccountService.DeleteAccount(bankAccount);
            return new BankAccountResult.Success();
        } catch (Exception e) {
            return new BankAccountResult.BankAccountDeletionError(e.getMessage());
        }
    }

    @Override
    public OperationResult CheckBalance(int userId, int accountId) {
        BankAccount account = bankAccountService.GetAccount(accountId);
        if (account == null || account.getUser().getId() != userId) {
            return new OperationResult.OperationError("Счет не найден или принадлежит другому пользователю.");
        }
        System.out.println("Баланс счета: " + account.getBalance());
        return new OperationResult.Success();
    }

    @Override
    public OperationResult Deposit(BankAccount bankAccount, Double amount) {
        try {
            boolean success = bankAccountService.Deposit(bankAccount.getId(), amount);
            if (!success) {
                return new OperationResult.OperationError("Deposit failed.");
            }
            operationService.SaveOperation(new Operation(bankAccount, OperationType.Deposit, amount));
            return new OperationResult.Success();
        } catch (Exception e) {
            return new OperationResult.OperationError(e.getMessage());
        }
    }

    @Override
    public OperationResult Withdraw(BankAccount bankAccount, Double amount) {
        try {
            boolean success = bankAccountService.Withdraw(bankAccount.getId(), amount);
            if (!success) {
                return new OperationResult.OperationError("Not enough balance.");
            }
            operationService.SaveOperation(new Operation(bankAccount, OperationType.Withdraw, amount));
            return new OperationResult.Success();
        } catch (Exception e) {
            return new OperationResult.OperationError(e.getMessage());
        }
    }

    @Override
    public OperationResult Transfer(BankAccount bankAccount1, BankAccount bankAccount2, Double amount) {
        try {
            User user1 = userService.GetUser(bankAccount1.getUser().getId());
            User user2 = userService.GetUser(bankAccount2.getUser().getId());

            double commissionRate = user1.getId().equals(user2.getId()) ? 0.00
                    : user1.getFriends().stream().anyMatch(f -> f.getId().equals(user2.getId())) ? 0.03
                    : 0.10;
            double commission = amount * commissionRate;
            double totalAmount = amount + commission;

            boolean success = bankAccountService.Transfer(bankAccount1.getId(), bankAccount2.getId(), totalAmount);
            if (!success) {
                return new OperationResult.OperationError("Insufficient funds, including commission.");
            }

            operationService.SaveOperation(new Operation(bankAccount1, OperationType.Transfer, totalAmount));
            operationService.SaveOperation(new Operation(bankAccount2, OperationType.Deposit, amount));

            return new OperationResult.Success();
        } catch (Exception e) {
            return new OperationResult.OperationError(e.getMessage());
        }
    }

    @Override
    public UserResult DeleteUserById(int id) {
        try {
            User user = userService.GetUser(id);
            if (user == null) {
                return new UserResult.UserDeletionError("Пользователь с данным ID не найден.");
            }

            userService.DeleteUser(user);
            return new UserResult.Success();
        } catch (Exception e) {
            return new UserResult.UserDeletionError("Ошибка при удалении пользователя: " + e.getMessage());
        }
    }
}
