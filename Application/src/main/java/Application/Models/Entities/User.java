package Application.Models.Entities;

import jakarta.persistence.*;
import lombok.*;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.Sex;
import java.util.*;

/**
 * Класс, представляющий пользователя системы.
 * Хранит информацию о пользователе, включая личные данные, список банковских счетов и друзей.
 */
@Entity
@Getter
@Table(name = "Users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    @Column(name = "hairColor")
    private HairColor hairType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BankAccount> bankAccounts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Friends",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "friendId")
    )
    private List<User> friends;

    /**
     * Конструктор для создания нового пользователя.
     * Генерирует уникальный идентификатор для пользователя и инициализирует его данные.
     *
     * @param login логин пользователя.
     * @param name имя пользователя.
     * @param age возраст пользователя.
     * @param sex пол пользователя.
     * @param hairType цвет волос пользователя.
     */
    public User(String login, String name, Integer age, Sex sex, HairColor hairType) {
        this.login = login;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.hairType = hairType;
        this.bankAccounts = new ArrayList<>();
        this.friends = new ArrayList<>();
    }
}

