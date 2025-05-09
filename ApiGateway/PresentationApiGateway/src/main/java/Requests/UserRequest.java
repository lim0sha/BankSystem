package Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import Models.Enums.Role;

@Getter
@AllArgsConstructor
public class UserRequest {

    private final String username;
    private final String password;
    private final Role role;

    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = null;
    }
}
