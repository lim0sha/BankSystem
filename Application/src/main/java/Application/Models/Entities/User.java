package Application.Models.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.Sex;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    @Column(name = "haircolor")
    private HairColor hairType;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
            , orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<BankAccount> bankAccounts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends = new ArrayList<>();

    public User(String login, String name, Integer age, Sex sex, HairColor hairType) {
        this.login = login;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.hairType = hairType;
    }

    public void addBankAccount(BankAccount bankAccount) {
        if (!bankAccounts.contains(bankAccount)) {
            bankAccounts.add(bankAccount);
            bankAccount.setUser(this);
        }
    }
}
