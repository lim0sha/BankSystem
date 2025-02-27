import Application.Abstractions.Repositories.IBankAccountRepository;
import Application.Abstractions.Repositories.IOperationRepository;
import Application.Contracts.ResultTypes.OperationResult;
import Application.Models.Entites.BankAccount;
import Application.Models.Entites.User;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.Sex;
import Application.Models.Utils.IdGenerator;
import Application.Services.UserService;
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
    private IBankAccountRepository bankAccountRepository;

    @Mock
    private IOperationRepository operationRepository;

    @InjectMocks
    private UserService userService;

    private BankAccount bankAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        User user = new User(new IdGenerator(), "lim0sha", "Sasha", 22, Sex.Male, HairColor.Brown);
        bankAccount = new BankAccount(new IdGenerator(), user);
        bankAccount.setBalance(100.0);
    }

    @Test
    void testWithdrawWithSufficientBalance() {
        Double withdrawAmount = 50.0;
        Double initialBalance = bankAccount.getBalance();

        when(bankAccountRepository.FindBankAccountById(bankAccount.getId())).thenReturn(bankAccount);
        when(bankAccountRepository.UpdateBankAccountBalance(bankAccount.getId(), initialBalance - withdrawAmount))
                .thenAnswer(invocation -> {
                    bankAccount.setBalance(initialBalance - withdrawAmount);
                    return null;
                });
        when(operationRepository.AddOperation(any())).thenReturn(new OperationResult.Success());

        OperationResult operationResult = userService.Withdraw(bankAccount, withdrawAmount);

        assertInstanceOf(OperationResult.Success.class, operationResult);
        assertEquals(initialBalance - withdrawAmount, bankAccount.getBalance(), 0.001);
        verify(bankAccountRepository, times(1)).UpdateBankAccountBalance(bankAccount.getId(), initialBalance - withdrawAmount);
        verify(operationRepository, times(1)).AddOperation(any());

        assertEquals(50.0, bankAccount.getBalance(), 0.001);
    }

    @Test
    void testWithdrawWithInsufficientBalance() {
        Double withdrawAmount = 150.0;

        when(bankAccountRepository.FindBankAccountById(bankAccount.getId())).thenReturn(bankAccount);

        OperationResult operationResult = userService.Withdraw(bankAccount, withdrawAmount);

        assertInstanceOf(OperationResult.OperationError.class, operationResult);
        assertEquals("Not enough balance", ((OperationResult.OperationError) operationResult).getMessage());
        verify(bankAccountRepository, times(0)).UpdateBankAccountBalance(any(), anyDouble());
        verify(operationRepository, times(0)).AddOperation(any());

        assertEquals(100.0, bankAccount.getBalance(), 0.001);
    }

    @Test
    void testDeposit() {
        Double depositAmount = 100.0;
        Double initialBalance = bankAccount.getBalance();

        when(bankAccountRepository.FindBankAccountById(bankAccount.getId())).thenReturn(bankAccount);
        when(bankAccountRepository.UpdateBankAccountBalance(bankAccount.getId(), initialBalance + depositAmount))
                .thenAnswer(invocation -> {
                    bankAccount.setBalance(initialBalance + depositAmount);
                    return null;
                });
        when(operationRepository.AddOperation(any())).thenReturn(new OperationResult.Success());

        OperationResult operationResult = userService.Deposit(bankAccount, depositAmount);

        assertInstanceOf(OperationResult.Success.class, operationResult);
        assertEquals(initialBalance + depositAmount, bankAccount.getBalance(), 0.001);
        verify(bankAccountRepository, times(1)).UpdateBankAccountBalance(bankAccount.getId(), initialBalance + depositAmount);
        verify(operationRepository, times(1)).AddOperation(any());

        assertEquals(200.0, bankAccount.getBalance(), 0.001);
    }
}