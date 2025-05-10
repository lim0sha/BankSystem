package Controllers;

import DTO.BankAccountDTO;
import DTO.OperationDTO;
import DTO.UserDTO;
import JWT.HttpAdminUtil;
import Requests.UserRequest;
import Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final RestTemplate restTemplate;
    private final UserService userService;
    private final HttpAdminUtil adminUtil;

    @Autowired
    public AdminController(RestTemplate restTemplate, UserService userService, HttpAdminUtil adminUtil) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.adminUtil = adminUtil;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/create")
    public ResponseEntity<String> CreateUser(@RequestBody UserRequest userRequest) {
        try {
            userService.CreateUser(userRequest.getUsername(), userRequest.getPassword(), userRequest.getRole());
            return ResponseEntity.ok("User created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> GetAllUsersFiltered(
            @RequestParam(required = false) String sex,
            @RequestParam(required = false) String hairColor
    ) {
        try {
            String url = "http://localhost:8080/data/users?sex=" + (sex != null ? sex : "") +
                    "&hairColor=" + (hairColor != null ? hairColor : "");

            ResponseEntity<List<UserDTO>> response = restTemplate.exchange(
                    url, HttpMethod.GET, adminUtil.withAuthHeaders(), new ParameterizedTypeReference<>() {}
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving users: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity<?> GetUserById(@PathVariable int id) {
        try {
            String url = "http://localhost:8080/users/" + id;
            return restTemplate.exchange(url, HttpMethod.GET, adminUtil.withAuthHeaders(), UserDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving user: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bankaccounts")
    public ResponseEntity<?> GetAllBankAccounts() {
        try {
            String url = "http://localhost:8080/data/accounts";
            ResponseEntity<List<BankAccountDTO>> response = restTemplate.exchange(
                    url, HttpMethod.GET, adminUtil.withAuthHeaders(), new ParameterizedTypeReference<>() {}
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving accounts: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bankaccounts/user/{userId}")
    public ResponseEntity<?> GetAccountsByUserId(@PathVariable int userId) {
        try {
            String url = "http://localhost:8080/data/users/" + userId + "/accounts";
            ResponseEntity<BankAccountDTO[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, adminUtil.withAuthHeaders(), BankAccountDTO[].class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving accounts for user: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bankaccounts/{id}/operations")
    public ResponseEntity<?> getOperationsByAccountId(
            @PathVariable int id,
            @RequestParam(required = false) String type
    ) {
        try {
            String url = "http://localhost:8080/data/operations?accountId=" + id;
            if (type != null && !type.isEmpty()) {
                url += "&type=" + type;
            }

            ResponseEntity<List<OperationDTO>> response = restTemplate.exchange(
                    url, HttpMethod.GET, adminUtil.withAuthHeaders(), new ParameterizedTypeReference<>() {}
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving operations: " + e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<?> Logout() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return ResponseEntity.ok("Admin " + authentication.getName() + " logged out.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during logout: " + e.getMessage());
        }
    }
}
