package Application.Models.Entites;

import lombok.*;
import Application.Models.Enums.HairColor;
import Application.Models.Enums.Sex;
import Application.Models.Utils.IdGenerator;

import java.util.ArrayList;

/**
 * Класс, представляющий пользователя системы.
 * Хранит информацию о пользователе, включая личные данные, список банковских счетов и друзей.
 */
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

    /**
     * Конструктор для создания нового пользователя.
     * Генерирует уникальный идентификатор для пользователя и инициализирует его данные.
     *
     * @param idGenerator генератор ID для создания уникального идентификатора пользователя.
     * @param login логин пользователя.
     * @param name имя пользователя.
     * @param age возраст пользователя.
     * @param sex пол пользователя.
     * @param hairType цвет волос пользователя.
     */
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
