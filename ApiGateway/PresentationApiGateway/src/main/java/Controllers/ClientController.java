package Controllers;

import DTO.BankAccountDTO;
import DTO.UserDTO;
import Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final RestTemplate restTemplate;
    private final UserService userService;

    @Autowired
    public ClientController(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.FindUserByUsername(username).getId();
    }

    // 1 - Получение информации о себе
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Long userId = getCurrentUserId();
        String url = "http://localhost:8080/users/" + userId;
        return restTemplate.getForEntity(url, UserDTO.class);
    }

    // 2 - Получение своих счетов
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/accounts")
    public ResponseEntity<BankAccountDTO[]> getMyAccounts() {
        Long userId = getCurrentUserId();
        String url = "http://localhost:8080/data/users/" + userId + "/accounts";
        ResponseEntity<BankAccountDTO[]> response = restTemplate.getForEntity(url, BankAccountDTO[].class);
        return ResponseEntity.ok(response.getBody());
    }

    // 3 - Получение конкретного счёта
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/accounts/{id}")
    public ResponseEntity<BankAccountDTO> getAccountById(@PathVariable int id) {
        String url = "http://localhost:8080/data/accounts/" + id;
        ResponseEntity<BankAccountDTO> response = restTemplate.getForEntity(url, BankAccountDTO.class);
        return ResponseEntity.ok(response.getBody());
    }

    // 4 - Добавление другого пользователя в друзья
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/friends/add/{otherId}")
    public ResponseEntity<String> addFriend(@PathVariable int otherId) {
        Long userId = getCurrentUserId();
        String url = "http://localhost:8080/interactions/friends/add/" + userId + "/" + otherId;
        return restTemplate.postForEntity(url, null, String.class);
    }

    // 5 - Удаление пользователя из друзей
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/friends/remove/{otherId}")
    public ResponseEntity<String> removeFriend(@PathVariable int otherId) {
        Long userId = getCurrentUserId();
        String url = "http://localhost:8080/interactions/friends/remove/" + userId + "/" + otherId;
        return restTemplate.postForEntity(url, null, String.class);
    }

    // 6.1 - Пополнение
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/accounts/{accountId}/deposit/{amount}")
    public ResponseEntity<String> deposit(@PathVariable int accountId, @PathVariable double amount) {
        String url = "http://localhost:8080/interactions/deposit/" + accountId + "/" + amount;
        return restTemplate.postForEntity(url, null, String.class);
    }

    // 6.2 - Снятие
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/accounts/{accountId}/withdraw/{amount}")
    public ResponseEntity<String> withdraw(@PathVariable int accountId, @PathVariable double amount) {
        String url = "http://localhost:8080/interactions/withdraw/" + accountId + "/" + amount;
        return restTemplate.postForEntity(url, null, String.class);
    }

    // 6.3 - Перевод
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/accounts/transfer/{fromId}/{toId}/{amount}")
    public ResponseEntity<String> transfer(
            @PathVariable int fromId,
            @PathVariable int toId,
            @PathVariable double amount
    ) {
        String url = "http://localhost:8080/interactions/transfer/" + fromId + "/" + toId + "/" + amount;
        return restTemplate.postForEntity(url, null, String.class);
    }

    // 7 - Деавторизация
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok("Client " + username + " logged out.");
    }
}