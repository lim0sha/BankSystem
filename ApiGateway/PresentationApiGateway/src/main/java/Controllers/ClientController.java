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

import java.util.Objects;

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

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Long userId = getCurrentUserId();
            String url = "http://localhost:8080/users/" + userId;
            return restTemplate.getForEntity(url, UserDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving current user: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/accounts")
    public ResponseEntity<?> getMyAccounts() {
        try {
            Long userId = getCurrentUserId();
            String url = "http://localhost:8080/data/users/" + userId + "/accounts";
            ResponseEntity<BankAccountDTO[]> response = restTemplate.getForEntity(url, BankAccountDTO[].class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving accounts: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/accounts/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable int id) {
        try {
            Long userId = getCurrentUserId();

            String userAccountsUrl = "http://localhost:8080/data/users/" + userId + "/accounts";
            ResponseEntity<BankAccountDTO[]> accountsResponse = restTemplate.getForEntity(userAccountsUrl, BankAccountDTO[].class);

            boolean ownsAccount = false;
            if (accountsResponse.getStatusCode().is2xxSuccessful()) {
                for (BankAccountDTO account : Objects.requireNonNull(accountsResponse.getBody())) {
                    if (account.getId() == id) {
                        ownsAccount = true;
                        break;
                    }
                }
            }

            if (!ownsAccount) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied to account.");
            }

            String url = "http://localhost:8080/data/accounts/" + id;
            return restTemplate.getForEntity(url, BankAccountDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving account: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/friends/add/{otherId}")
    public ResponseEntity<?> addFriend(@PathVariable int otherId) {
        try {
            Long userId = getCurrentUserId();
            String url = "http://localhost:8080/interactions/friends/add/" + userId + "/" + otherId;
            return restTemplate.postForEntity(url, null, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error adding friend: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/friends/remove/{otherId}")
    public ResponseEntity<?> removeFriend(@PathVariable int otherId) {
        try {
            Long userId = getCurrentUserId();
            String url = "http://localhost:8080/interactions/friends/remove/" + userId + "/" + otherId;
            return restTemplate.postForEntity(url, null, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error removing friend: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/accounts/{accountId}/deposit/{amount}")
    public ResponseEntity<?> deposit(@PathVariable int accountId, @PathVariable double amount) {
        try {
            String url = "http://localhost:8080/interactions/deposit/" + accountId + "/" + amount;
            return restTemplate.postForEntity(url, null, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during deposit: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/accounts/{accountId}/withdraw/{amount}")
    public ResponseEntity<?> withdraw(@PathVariable int accountId, @PathVariable double amount) {
        try {
            String url = "http://localhost:8080/interactions/withdraw/" + accountId + "/" + amount;
            return restTemplate.postForEntity(url, null, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during withdrawal: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/accounts/transfer/{fromId}/{toId}/{amount}")
    public ResponseEntity<?> transfer(
            @PathVariable int fromId,
            @PathVariable int toId,
            @PathVariable double amount
    ) {
        try {
            String url = "http://localhost:8080/interactions/transfer/" + fromId + "/" + toId + "/" + amount;
            return restTemplate.postForEntity(url, null, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during transfer: " + e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            return ResponseEntity.ok("Client " + username + " logged out.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during logout: " + e.getMessage());
        }
    }
}
