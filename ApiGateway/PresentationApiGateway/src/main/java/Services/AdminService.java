package Services;

import DTO.BankAccountDTO;
import DTO.OperationDTO;
import DTO.UserDTO;
import JWT.HttpAdminUtil;
import Requests.UserFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class AdminService {

    private final RestTemplate restTemplate;
    private final HttpAdminUtil adminUtil;

    @Autowired
    public AdminService(RestTemplate restTemplate, HttpAdminUtil adminUtil) {
        this.restTemplate = restTemplate;
        this.adminUtil = adminUtil;
    }

    public List<UserDTO> getFilteredUsers(UserFilterRequest filterRequest) {
        String url = "http://localhost:8080/data/users?sex=" +
                (filterRequest.getSex() != null ? filterRequest.getSex() : "") +
                "&hairColor=" + (filterRequest.getHairColor() != null ? filterRequest.getHairColor() : "");

        ResponseEntity<List<UserDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                adminUtil.withAuthHeaders(),
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }

    public UserDTO getUserById(int id) {
        String url = "http://localhost:8080/users/" + id;
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                adminUtil.withAuthHeaders(),
                UserDTO.class
        );
        return response.getBody();
    }

    public List<BankAccountDTO> getAllBankAccounts() {
        String url = "http://localhost:8080/data/accounts";
        ResponseEntity<List<BankAccountDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                adminUtil.withAuthHeaders(),
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }

    public List<BankAccountDTO> getAccountsByUserId(int userId) {
        String url = "http://localhost:8080/data/users/" + userId + "/accounts";
        ResponseEntity<BankAccountDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                adminUtil.withAuthHeaders(),
                BankAccountDTO[].class
        );
        return Arrays.asList(response.getBody());
    }

    public List<OperationDTO> getOperationsByAccountId(int accountId, String type) {
        String url = "http://localhost:8080/data/operations?accountId=" + accountId;
        if (type != null && !type.isEmpty()) {
            url += "&type=" + type;
        }

        ResponseEntity<List<OperationDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                adminUtil.withAuthHeaders(),
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }
}
