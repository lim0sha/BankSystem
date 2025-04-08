package Presentation.Controllers;

import Application.Models.Entities.BankAccount;
import Application.Models.Entities.User;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.OperationType;
import Application.Models.Enums.Sex;
import Presentation.DTO.BankAccountDTO;
import Presentation.DTO.OperationDTO;
import Presentation.DTO.UserDTO;
import Presentation.Interfaces.IUserController;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data")
public class UserDataController {

    private final IUserController userController;

    @Autowired
    public UserDataController(IUserController userController) {
        this.userController = userController;
    }

    @Operation(summary = "Получить всех пользователей с фильтрами", description = "Возвращает список пользователей, отфильтрованных по полу и цвету волос")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsersFiltered(
            @Parameter(description = "Пол пользователя") @RequestParam(required = false) Sex sex,
            @Parameter(description = "Цвет волос пользователя") @RequestParam(required = false) HairColor hairColor
    ) {
        List<User> users = userController.GetAllUsersFiltered(sex, hairColor);
        List<UserDTO> dtos = users.stream().map(UserDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Получить друзей пользователя", description = "Возвращает список друзей по ID пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список друзей", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID пользователя")
    })
    @GetMapping("/users/{id}/friends")
    public ResponseEntity<List<UserDTO>> getFriends(@Parameter(description = "ID пользователя") @PathVariable int id) {
        List<User> friends = userController.GetFriends(id);
        List<UserDTO> dtos = friends.stream().map(UserDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Получить банковские счета пользователя", description = "Возвращает все банковские счета, принадлежащие пользователю по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список счетов", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BankAccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID пользователя")
    })
    @GetMapping("/users/{id}/accounts")
    public ResponseEntity<List<BankAccountDTO>> getAccountsByUser(@Parameter(description = "ID пользователя") @PathVariable int id) {
        List<BankAccount> accounts = userController.GetUserBankAccounts(id);
        List<BankAccountDTO> dtos = accounts.stream().map(BankAccountDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Получить все банковские счета", description = "Возвращает список всех банковских счетов в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список счетов", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BankAccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка при обработке запроса")
    })
    @GetMapping("/accounts")
    public ResponseEntity<List<BankAccountDTO>> getAllAccounts() {
        List<BankAccount> accounts = userController.GetAllAccounts();
        List<BankAccountDTO> dtos = accounts.stream().map(BankAccountDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Получить операции (транзакции)", description = "Возвращает список операций с фильтрацией по типу и ID счета")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список операций", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
    })
    @GetMapping("/operations")
    public ResponseEntity<List<OperationDTO>> getTransactions(
            @Parameter(description = "Тип операции (Deposit, Withdraw, Transfer)") @RequestParam(required = false) OperationType type,
            @Parameter(description = "ID банковского счета") @RequestParam(required = false) Integer accountId
    ) {
        List<Application.Models.Entities.Operation> txs = userController.GetFilteredOperations(type, accountId);
        List<OperationDTO> dtos = txs.stream().map(OperationDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }
}
