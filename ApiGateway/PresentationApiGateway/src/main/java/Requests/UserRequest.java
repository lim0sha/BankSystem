package Requests;

import Models.Enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest {
    private String username;
    private String password;
    private Role role;
}
