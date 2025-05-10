package Controllers;

import Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        return clientService.getCurrentUser();
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/accounts")
    public ResponseEntity<?> getMyAccounts() {
        return clientService.getMyAccounts();
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/accounts/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable int id) {
        return clientService.getAccountById(id);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/friends/add/{otherId}")
    public ResponseEntity<?> addFriend(@PathVariable int otherId) {
        return clientService.addFriend(otherId);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/friends/remove/{otherId}")
    public ResponseEntity<?> removeFriend(@PathVariable int otherId) {
        return clientService.removeFriend(otherId);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/accounts/{accountId}/deposit/{amount}")
    public ResponseEntity<?> deposit(@PathVariable int accountId, @PathVariable double amount) {
        return clientService.deposit(accountId, amount);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/accounts/{accountId}/withdraw/{amount}")
    public ResponseEntity<?> withdraw(@PathVariable int accountId, @PathVariable double amount) {
        return clientService.withdraw(accountId, amount);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/accounts/transfer/{fromId}/{toId}/{amount}")
    public ResponseEntity<?> transfer(
            @PathVariable int fromId,
            @PathVariable int toId,
            @PathVariable double amount
    ) {
        return clientService.transfer(fromId, toId, amount);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Client " + authentication.getName() + " logged out.");
    }
}