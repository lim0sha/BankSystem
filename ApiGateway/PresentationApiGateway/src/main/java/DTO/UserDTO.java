package DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private String login;
    private String name;
    private Integer age;
    private String sex;
    private String hairType;
    private List<Integer> friendIds;
    private List<Integer> bankAccountIds;
}
