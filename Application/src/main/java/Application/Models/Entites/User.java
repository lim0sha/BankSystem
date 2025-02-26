package Application.Models.Entites;

import lombok.*;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.Sex;
import Application.Models.Utils.IdGenerator;

import java.util.ArrayList;

@Getter
@Setter
public class User {
    private Integer id;

    private String login;

    private String name;

    private Integer age;

    private Sex sex;

    private HairColor hairType;

    private ArrayList<Integer> bankAccounts = new ArrayList<>();

    private ArrayList<User> friends = new ArrayList<>();

    public User(
            IdGenerator idGenerator,
            String login,
            String name,
            Integer age,
            Sex sex,
            HairColor hairType
    ) {
        this.id = idGenerator.generateUserId();
        this.login = login;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.hairType = hairType;
        this.bankAccounts = bankAccounts != null ? bankAccounts : new ArrayList<>();
        this.friends = friends != null ? friends : new ArrayList<>();
    }
}
