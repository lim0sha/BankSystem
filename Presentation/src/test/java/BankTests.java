import Application.Managers.UserManager;
import Application.ResultTypes.OperationResult;
import Application.Models.Entities.BankAccount;
import Application.Models.Entities.User;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.Sex;
import Presentation.Controllers.UserController;
import Services.BankAccountService;
import Services.OperationService;
import Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

public class BankTests {

    @Mock
    private UserManager userManager;

    @Mock
    private UserService userService;

    @Mock
    private BankAccountService bankAccountService;

    @Mock
    private OperationService operationService;

    @InjectMocks
    private UserController userController;

    private BankAccount bankAccount;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User("lim0sha", "Sasha", 22, Sex.Male, HairColor.Brown);
        bankAccount = new BankAccount(user);
        bankAccount.setId(1);
        bankAccount.setBalance(100.0);
    }

    @Test
    void testWithdrawWithSufficientBalance() {
        double withdrawAmount = 50.0;
        when(bankAccountService.Withdraw(bankAccount.getId(), withdrawAmount)).thenReturn(true);
        doNothing().when(operationService).SaveOperation(any());

        OperationResult operationResult = userController.Withdraw(bankAccount, withdrawAmount);

        assertInstanceOf(OperationResult.Success.class, operationResult);
        verify(bankAccountService, times(1)).Withdraw(bankAccount.getId(), withdrawAmount);
        verify(operationService, times(1)).SaveOperation(any());
    }

    @Test
    void testWithdrawWithInsufficientBalance() {
        double withdrawAmount = 150.0;
        when(bankAccountService.Withdraw(bankAccount.getId(), withdrawAmount)).thenReturn(false);

        OperationResult operationResult = userController.Withdraw(bankAccount, withdrawAmount);

        assertInstanceOf(OperationResult.OperationError.class, operationResult);
        assertEquals("Not enough balance.", ((OperationResult.OperationError) operationResult).getMessage());
        verify(bankAccountService, times(1)).Withdraw(bankAccount.getId(), withdrawAmount);
        verify(operationService, times(0)).SaveOperation(any()); // SaveOperation не должен вызываться
    }

    @Test
    void testDeposit() {
        double depositAmount = 100.0;
        when(bankAccountService.Deposit(bankAccount.getId(), depositAmount)).thenReturn(true);
        doNothing().when(operationService).SaveOperation(any());

        OperationResult operationResult = userController.Deposit(bankAccount, depositAmount);

        assertInstanceOf(OperationResult.Success.class, operationResult);
        verify(bankAccountService, times(1)).Deposit(bankAccount.getId(), depositAmount);
        verify(operationService, times(1)).SaveOperation(any());
    }
}