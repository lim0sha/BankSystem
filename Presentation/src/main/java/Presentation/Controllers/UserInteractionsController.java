package Presentation.Controllers;

import Application.Models.Entities.BankAccount;
import Application.ResultTypes.OperationResult;
import Presentation.Interfaces.IBaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interactions")
public class UserInteractionsController {

    private final IBaseController baseController;

    @Autowired
    public UserInteractionsController(IBaseController baseController) {
        this.baseController = baseController;
    }

    @Operation(summary = "Добавить друга", description = "Добавляет пользователя в друзья")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Друг добавлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка добавления")
    })
    @PostMapping("/friends/add/{userId}/{otherId}")
    public ResponseEntity<String> addFriend(
            @PathVariable int userId,
            @PathVariable int otherId
    ) {
        baseController.AddFriend(userId, otherId);
        return ResponseEntity.ok("Friend added.");
    }

    @Operation(summary = "Удалить друга", description = "Удаляет пользователя из друзей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Друг удалён"),
            @ApiResponse(responseCode = "400", description = "Ошибка удаления")
    })
    @PostMapping("/friends/remove/{userId}/{otherId}")
    public ResponseEntity<String> removeFriend(
            @PathVariable int userId,
            @PathVariable int otherId
    ) {
        baseController.RemoveFriend(userId, otherId);
        return ResponseEntity.ok("Friend removed.");
    }

    @Operation(summary = "Пополнить счёт", description = "Осуществляет пополнение счёта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пополнение выполнено"),
            @ApiResponse(responseCode = "400", description = "Ошибка пополнения")
    })
    @PostMapping("/deposit/{accountId}/{amount}")
    public ResponseEntity<String> deposit(
            @PathVariable int accountId,
            @PathVariable double amount
    ) {
        BankAccount account = baseController.GetBankAccountById(accountId);
        OperationResult result = baseController.Deposit(account, amount);

        if (result instanceof OperationResult.Success) {
            return ResponseEntity.ok("Deposit successful.");
        }
        return ResponseEntity.badRequest().body(result.toString());
    }

    @Operation(summary = "Снять со счёта", description = "Осуществляет снятие средств со счёта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Снятие выполнено"),
            @ApiResponse(responseCode = "400", description = "Ошибка снятия")
    })
    @PostMapping("/withdraw/{accountId}/{amount}")
    public ResponseEntity<String> withdraw(
            @PathVariable int accountId,
            @PathVariable double amount
    ) {
        BankAccount account = baseController.GetBankAccountById(accountId);
        OperationResult result = baseController.Withdraw(account, amount);

        if (result instanceof OperationResult.Success) {
            return ResponseEntity.ok("Withdrawal successful.");
        }
        return ResponseEntity.badRequest().body(result.toString());
    }

    @Operation(summary = "Перевод между счетами", description = "Выполняет перевод между счетами с учётом комиссии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Перевод выполнен"),
            @ApiResponse(responseCode = "400", description = "Ошибка перевода")
    })
    @PostMapping("/transfer/{fromAccountId}/{toAccountId}/{amount}")
    public ResponseEntity<String> transfer(
            @PathVariable int fromAccountId,
            @PathVariable int toAccountId,
            @PathVariable double amount
    ) {
        BankAccount fromAccount = baseController.GetBankAccountById(fromAccountId);
        BankAccount toAccount = baseController.GetBankAccountById(toAccountId);

        OperationResult result = baseController.Transfer(fromAccount, toAccount, amount);

        if (result instanceof OperationResult.Success) {
            return ResponseEntity.ok("Transfer successful.");
        }
        return ResponseEntity.badRequest().body(result.toString());
    }
}
