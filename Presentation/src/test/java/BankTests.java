import Application.Managers.UserManager;
import Application.ResultTypes.OperationResult;
import Application.Models.Entities.BankAccount;
import Application.Models.Entities.User;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.Sex;
import DataAccess.Services.BankAccountService;
import DataAccess.Services.Interfaces.IUserService;
import DataAccess.Services.OperationService;
import Presentation.Controllers.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankTests {

    @Mock
    private BankAccountService bankAccountService;

    @Mock
    private OperationService operationService;

    @Mock
    private UserManager userManager;

    @Mock
    private IUserService userService;

    @InjectMocks
    private BaseController baseController;

    private static BankAccount bankAccount;
    private static User user;

    @BeforeAll
    static void init() {
        user = new User("lim0sha", "Sasha", 22, Sex.Male, HairColor.Brown);
        bankAccount = new BankAccount(user);
        bankAccount.setId(1);
        bankAccount.setBalance(100.0);
    }


    @Test
    public void testWithdrawWithSufficientBalance() {
        double withdrawAmount = 50.0;
        when(bankAccountService.Withdraw(bankAccount.getId(), withdrawAmount)).thenReturn(true);
        doNothing().when(operationService).SaveOperation(any());

        OperationResult operationResult = baseController.Withdraw(bankAccount, withdrawAmount);

        assertInstanceOf(OperationResult.Success.class, operationResult);
        verify(bankAccountService, times(1)).Withdraw(bankAccount.getId(), withdrawAmount);
        verify(operationService, times(1)).SaveOperation(any());
    }

    @Test
    public void testWithdrawWithInsufficientBalance() {
        double withdrawAmount = 150.0;
        when(bankAccountService.Withdraw(bankAccount.getId(), withdrawAmount)).thenReturn(false);

        OperationResult operationResult = baseController.Withdraw(bankAccount, withdrawAmount);

        assertInstanceOf(OperationResult.OperationError.class, operationResult);
        assertEquals("Not enough balance.", ((OperationResult.OperationError) operationResult).getMessage());
        verify(bankAccountService, times(1)).Withdraw(bankAccount.getId(), withdrawAmount);
        verify(operationService, times(0)).SaveOperation(any());
    }

    @Test
    public void testDeposit() {
        double depositAmount = 100.0;
        when(bankAccountService.Deposit(bankAccount.getId(), depositAmount)).thenReturn(true);
        doNothing().when(operationService).SaveOperation(any());

        OperationResult operationResult = baseController.Deposit(bankAccount, depositAmount);

        assertInstanceOf(OperationResult.Success.class, operationResult);
        verify(bankAccountService, times(1)).Deposit(bankAccount.getId(), depositAmount);
        verify(operationService, times(1)).SaveOperation(any());
    }
}