package Presentation.DTO;

import Application.Models.Entities.BankAccount;
import Application.Models.Entities.User;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class UserDTO {
    private final Integer id;
    private final String login;
    private final String name;
    private final Integer age;
    private final Sex sex;
    private final HairColor hairType;
    private final List<Integer> friendIds;
    private final List<Integer> bankAccountIds;

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.name = user.getName();
        this.age = user.getAge();
        this.sex = user.getSex();
        this.hairType = user.getHairType();
        this.friendIds = user.getFriends().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        this.bankAccountIds = user.getBankAccounts().stream()
                .map(BankAccount::getId)
                .collect(Collectors.toList());
    }
}
