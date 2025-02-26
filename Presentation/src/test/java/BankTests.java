import Application.Abstractions.Repositories.IBankAccountRepository;
import Application.Abstractions.Repositories.IOperationRepository;
import Application.Abstractions.Repositories.IUserRepository;
import Application.Contracts.Interfaces.IUserService;
import Application.Contracts.ResultTypes.OperationResult;
import Application.Managers.UserManager;
import Application.Models.Entites.BankAccount;
import Application.Models.Entites.User;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.Sex;
import Application.Models.Utils.IdGenerator;
import Application.Services.UserService;
import DataAccess.BankAccountRepository;
import DataAccess.OperationRepository;
import DataAccess.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankTests {

    @Test
    public void Scenario1() {
        IdGenerator userIdGenerator = new IdGenerator();
        IdGenerator bankAccountIdGenerator = new IdGenerator();
        IUserRepository userRepository = new UserRepository();
        IOperationRepository operationRepository = new OperationRepository();
        IBankAccountRepository bankAccountRepository = new BankAccountRepository();
        var user1 = new User(userIdGenerator, "test_login", "lim0sha", 22, Sex.Male, HairColor.Brown);
        var bankAccount1 = new BankAccount(bankAccountIdGenerator, user1);
        IUserService userService = new UserService(userRepository, bankAccountRepository, operationRepository, new UserManager());

        userService.CreateUser(user1);
        userService.addBankAccount(user1, bankAccount1);
        userService.Deposit(bankAccount1, 239.00);
        userService.Withdraw(bankAccount1, 52.00);

        assertEquals(187.00, bankAccountRepository.FindBankAccountById(bankAccount1.getId()).getBalance());
    }

    @Test
    public void Scenario2() {
        IdGenerator userIdGenerator = new IdGenerator();
        IdGenerator bankAccountIdGenerator = new IdGenerator();
        IUserRepository userRepository = new UserRepository();
        IOperationRepository operationRepository = new OperationRepository();
        IBankAccountRepository bankAccountRepository = new BankAccountRepository();
        var user1 = new User(userIdGenerator, "test_login", "lim0sha", 22, Sex.Male, HairColor.Brown);
        var bankAccount1 = new BankAccount(bankAccountIdGenerator, user1);
        IUserService userService = new UserService(userRepository, bankAccountRepository, operationRepository, new UserManager());

        userService.CreateUser(user1);
        userService.addBankAccount(user1, bankAccount1);
        userService.Deposit(bankAccount1, 52.00);
        var withdrawResult = userService.Withdraw(bankAccount1, 239.00);

        assertEquals(OperationResult.OperationError.class, withdrawResult.getClass());
    }

    @Test
    public void Scenario3() {
        IdGenerator userIdGenerator = new IdGenerator();
        IdGenerator bankAccountIdGenerator = new IdGenerator();
        IUserRepository userRepository = new UserRepository();
        IOperationRepository operationRepository = new OperationRepository();
        IBankAccountRepository bankAccountRepository = new BankAccountRepository();
        var user1 = new User(userIdGenerator, "test_login", "lim0sha", 22, Sex.Male, HairColor.Brown);
        var bankAccount1 = new BankAccount(bankAccountIdGenerator, user1);
        IUserService userService = new UserService(userRepository, bankAccountRepository, operationRepository, new UserManager());

        userService.CreateUser(user1);
        userService.addBankAccount(user1, bankAccount1);
        userService.Deposit(bankAccount1, 52.00);
        userService.Deposit(bankAccount1, 52.00);

        assertEquals(104.00, bankAccountRepository.FindBankAccountById(bankAccount1.getId()).getBalance());
    }
}
