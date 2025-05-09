package Controllers;

import DTO.BankAccountDTO;
import DTO.OperationDTO;
import DTO.UserDTO;
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

    @Autowired
    public AdminController(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    // 1 - Создание пользователя
    // 2 - Создание администратора
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/create")
    public ResponseEntity<String> CreateUser(@RequestBody UserRequest userRequest) {
        try {
            String url = "http://localhost:8080/users/create";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UserRequest> requestHttpEntity = new HttpEntity<>(userRequest, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, requestHttpEntity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(response.getStatusCode())
                        .body("Failed to create new user.");
            }

            System.out.println("User created successfully.");
            userService.CreateUser(userRequest.getUsername(), userRequest.getPassword(), userRequest.getRole());
            return ResponseEntity.ok("Localhost user created.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
        }
    }

    // 3 - Получение информации о всех пользователях с фильтрацией по гендеру и цвету волос
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> GetAllUsersFiltered(
            @RequestParam(required = false) String sex,
            @RequestParam(required = false) String hairColor
    ) {
        String url = "http://localhost:8080/data/users?sex=" + (sex != null ? sex : "") +
                "&hairColor=" + (hairColor != null ? hairColor : "");

        ResponseEntity<List<UserDTO>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDTO>>() {}
        );

        List<UserDTO> users  = response.getBody();
        return ResponseEntity.ok(users);
    }

    // 4 - Получение информации о пользователе по его идентификатору
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> GetUserById(@PathVariable int id) {
        String url = "http://localhost:8080/users/" + id;
        return restTemplate.getForEntity(url, UserDTO.class);
    }


    // 5 - Получение информации о всех счётах пользователей
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bankaccounts")
    public ResponseEntity<List<BankAccountDTO>> GetAllBankAccounts() {
        String url = "http://localhost:8080/data/accounts";
        ResponseEntity<List<BankAccountDTO>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );
        return ResponseEntity.ok(response.getBody());
    }

    // 6 - Получение информации о счетах по идентификатору пользователя
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bankaccounts/user/{userId}")
    public ResponseEntity<?> GetAccountsByUserId(@PathVariable int userId) {
        String url = "http://localhost:8080/data/users/" + userId + "/accounts";
        ResponseEntity<BankAccountDTO[]> response = restTemplate.getForEntity(url, BankAccountDTO[].class);
        return ResponseEntity.ok(response.getBody());
    }

    // 7 - Получение информации о счёте со списком его операций по идентификатору счёта
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bankaccounts/{id}/operations")
    public ResponseEntity<List<OperationDTO>> getOperationsByAccountId(
            @PathVariable int id,
            @RequestParam(required = false) String type
    ) {
        String url = "http://localhost:8080/data/operations?accountId=" + id;
        if (type != null && !type.isEmpty()) {
            url += "&type=" + type;
        }

        ResponseEntity<List<OperationDTO>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<OperationDTO>>() {}
        );

        return ResponseEntity.ok(response.getBody());
    }

    // 8 - Деавторизация
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<String> Logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Admin " + authentication.getName() + " logged out.");
    }
}
