package Services;

import DTO.BankAccountDTO;
import DTO.UserDTO;
import JWT.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ClientService {

    private final RestTemplate restTemplate;
    private final HttpClientUtil clientUtil;
    private final UserService userService;

    @Autowired
    public ClientService(RestTemplate restTemplate, HttpClientUtil clientUtil, UserService userService) {
        this.restTemplate = restTemplate;
        this.clientUtil = clientUtil;
        this.userService = userService;
    }

    public ResponseEntity<UserDTO> getCurrentUser() {
        Long userId = clientUtil.getCurrentUserId(userService);
        String url = "http://localhost:8080/users/" + userId;
        return restTemplate.exchange(url, HttpMethod.GET, clientUtil.withAuthHeaders(), UserDTO.class);
    }

    public ResponseEntity<BankAccountDTO[]> getMyAccounts() {
        Long userId = clientUtil.getCurrentUserId(userService);
        String url = "http://localhost:8080/data/users/" + userId + "/accounts";
        return restTemplate.exchange(url, HttpMethod.GET, clientUtil.withAuthHeaders(), BankAccountDTO[].class);
    }

    public ResponseEntity<?> getAccountById(int id) {
        Long userId = clientUtil.getCurrentUserId(userService);
        String userAccountsUrl = "http://localhost:8080/data/users/" + userId + "/accounts";
        ResponseEntity<BankAccountDTO[]> accountsResponse = restTemplate.exchange(
                userAccountsUrl, HttpMethod.GET, clientUtil.withAuthHeaders(), BankAccountDTO[].class);

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
        return restTemplate.exchange(url, HttpMethod.GET, clientUtil.withAuthHeaders(), BankAccountDTO.class);
    }

    public ResponseEntity<String> addFriend(int otherId) {
        Long userId = clientUtil.getCurrentUserId(userService);
        String url = "http://localhost:8080/interactions/friends/add/" + userId + "/" + otherId;
        return restTemplate.exchange(url, HttpMethod.POST, clientUtil.withAuthHeaders(), String.class);
    }

    public ResponseEntity<String> removeFriend(int otherId) {
        Long userId = clientUtil.getCurrentUserId(userService);
        String url = "http://localhost:8080/interactions/friends/remove/" + userId + "/" + otherId;
        return restTemplate.exchange(url, HttpMethod.POST, clientUtil.withAuthHeaders(), String.class);
    }

    public ResponseEntity<String> deposit(int accountId, double amount) {
        String url = "http://localhost:8080/interactions/deposit/" + accountId + "/" + amount;
        return restTemplate.exchange(url, HttpMethod.POST, clientUtil.withAuthHeaders(), String.class);
    }

    public ResponseEntity<String> withdraw(int accountId, double amount) {
        String url = "http://localhost:8080/interactions/withdraw/" + accountId + "/" + amount;
        return restTemplate.exchange(url, HttpMethod.POST, clientUtil.withAuthHeaders(), String.class);
    }

    public ResponseEntity<String> transfer(int fromId, int toId, double amount) {
        String url = "http://localhost:8080/interactions/transfer/" + fromId + "/" + toId + "/" + amount;
        return restTemplate.exchange(url, HttpMethod.POST, clientUtil.withAuthHeaders(), String.class);
    }
}
