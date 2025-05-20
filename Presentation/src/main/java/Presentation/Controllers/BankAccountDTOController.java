package Presentation.Controllers;

import Application.Models.Entities.BankAccount;
import Application.Models.Entities.User;
import Application.ResultTypes.BankAccountResult;
import Presentation.DTO.BankAccountDTO;
import Presentation.DTO.CreateBankAccountDTO;
import Presentation.Interfaces.IBaseController;
import Presentation.Kafka.Services.KafkaProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/bankaccounts", produces = "application/json")
public class BankAccountDTOController {

    private final IBaseController baseController;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public BankAccountDTOController(IBaseController baseController, KafkaProducerService kafkaProducerService) {
        this.baseController = baseController;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Operation(summary = "Получить счет по ID", description = "Получает информацию о счете по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Счет найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BankAccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Счет не найден")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<BankAccountDTO> getBankAccountById(@Parameter(description = "ID счета") @PathVariable int id) {
        BankAccount account = baseController.GetBankAccountById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new BankAccountDTO(account));
    }

    @Operation(summary = "Создать новый счет", description = "Создает новый счет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Счет создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createBankAccount(@RequestBody CreateBankAccountDTO dto) {
        try {
            BankAccount account = new BankAccount();
            account.setId(null);
            account.setBalance(dto.getBalance());

            User user = baseController.GetUserById(dto.getUserId());
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found with ID: " + dto.getUserId());
            }

            account.setUser(user);
            account.setUserLogin(user.getLogin());

            BankAccountResult result = baseController.AddBankAccount(user.getId(), account);
            if (result instanceof BankAccountResult.Success) {
                kafkaProducerService.sendEvent("account-topic", String.valueOf(account.getId()), account);
                return ResponseEntity.status(HttpStatus.CREATED).body(new BankAccountDTO(account));
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @Operation(summary = "Обновить данные счета", description = "Обновляет данные о счете по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Счет обновлен", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BankAccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Счет не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateBankAccount(@Parameter(description = "ID счета") @PathVariable int id,
                                               @RequestBody BankAccount account) {
        BankAccount existingAccount = baseController.GetBankAccountById(id);
        if (existingAccount == null) {
            return ResponseEntity.notFound().build();
        }

        existingAccount.setBalance(account.getBalance());
        existingAccount.setUser(account.getUser());

        BankAccountResult result = baseController.UpdateBankAccount(existingAccount);
        if (result instanceof BankAccountResult.Success) {
            kafkaProducerService.sendEvent("account-topic", String.valueOf(existingAccount.getId()), existingAccount);
            return ResponseEntity.ok(new BankAccountDTO(existingAccount));
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(new BankAccountDTO(account));
        }
    }

    @Operation(summary = "Удалить счет", description = "Удаляет счет по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Счет удален"),
            @ApiResponse(responseCode = "404", description = "Счет не найден")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteBankAccount(@Parameter(description = "ID счета") @PathVariable int id) {
        BankAccount account = baseController.GetBankAccountById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        BankAccountResult result = baseController.RemoveBankAccount(account.getUser().getId(), account);
        if (result instanceof BankAccountResult.Success) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(new BankAccountDTO(account));
        }
    }
}
