package Presentation.Controllers;

import Application.Models.Entities.BankAccount;
import Application.Models.Entities.Operation;
import Application.Models.Entities.User;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.OperationType;
import Application.Models.Enums.Sex;
import Presentation.DTO.BankAccountDTO;
import Presentation.DTO.OperationDTO;
import Presentation.DTO.UserDTO;
import Presentation.Interfaces.IUserController;
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

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsersFiltered(
            @RequestParam(required = false) Sex sex,
            @RequestParam(required = false) HairColor hairColor
    ) {
        List<User> users = userController.GetAllUsersFiltered(sex, hairColor);
        List<UserDTO> dtos = users.stream().map(UserDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/users/{id}/friends")
    public ResponseEntity<List<UserDTO>> getFriends(@PathVariable int id) {
        List<User> friends = userController.GetFriends(id);
        List<UserDTO> dtos = friends.stream().map(UserDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/users/{id}/accounts")
    public ResponseEntity<List<BankAccountDTO>> getAccountsByUser(@PathVariable int id) {
        List<BankAccount> accounts = userController.GetUserBankAccounts(id);
        List<BankAccountDTO> dtos = accounts.stream().map(BankAccountDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<BankAccountDTO>> getAllAccounts() {
        List<BankAccount> accounts = userController.GetAllAccounts();
        List<BankAccountDTO> dtos = accounts.stream().map(BankAccountDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/operations")
    public ResponseEntity<List<OperationDTO>> getTransactions(
            @RequestParam(required = false) OperationType type,
            @RequestParam(required = false) Integer accountId
    ) {
        List<Operation> txs = userController.GetFilteredOperations(type, accountId);
        List<OperationDTO> dtos = txs.stream().map(OperationDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }
}

