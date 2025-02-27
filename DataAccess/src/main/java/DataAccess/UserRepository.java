package DataAccess;

import lombok.NoArgsConstructor;
import Application.Abstractions.Repositories.IUserRepository;
import Application.Contracts.ResultTypes.UserResult;
import Application.Models.Entites.User;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Реализация репозитория для управления пользователями.
 * Хранит пользователей в памяти (в списке) и реализует операции CRUD для сущности {@link User}.
 *
 * <p>Этот класс использует {@link ArrayList} для хранения данных и позволяет добавлять, обновлять, искать и удалять пользователей.</p>
 */
@NoArgsConstructor
public class UserRepository implements IUserRepository {

    private final ArrayList<User> _users = new ArrayList<>();

    /**
     * Добавляет нового пользователя в хранилище.
     *
     * @param user объект пользователя, который будет добавлен.
     * @return результат операции добавления, может быть ошибка создания пользователя, если пользователь равен {@code null}.
     */
    @Override
    public UserResult AddUser(User user) {
        if (user == null) {
            return new UserResult.UserCreationError("User can not be null");
        }
        _users.add(user);
        return new UserResult.Success();
    }

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя, которого нужно найти.
     * @return найденный пользователь или {@code null}, если пользователь с таким идентификатором не существует.
     */
    @Override
    public User FindUserById(Integer id) {
        if (id < 0) {
            return null;
        }
        for (User user : _users) {
            if (Objects.equals(user.getId(), id)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Обновляет данные пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя, которого нужно обновить.
     * @param userUpdate объект с новыми данными пользователя.
     * @return результат операции обновления, может быть ошибка, если параметры обновления неверны.
     */
    @Override
    public UserResult UpdateUser(Integer id, User userUpdate) {
        if (userUpdate == null || id < 0) {
            return new UserResult.UserUpdateError("Invalid update parameters");
        }

        for (int i = 0; i < _users.size(); ++i) {
            User existingUser = _users.get(i);
            if (Objects.equals(existingUser.getId(), id)) {
                _users.set(i, userUpdate);
                return new UserResult.Success();
            }
        }

        return new UserResult.UserUpdateError("User update failed ");
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя, которого нужно удалить.
     * @return результат операции удаления, может быть ошибка, если параметры удаления неверны.
     */
    @Override
    public UserResult DeleteUser(Integer id) {
        if (id < 0) {
            return new UserResult.UserDeletionError("Invalid delete parameters");
        }
        _users.removeIf(user -> Objects.equals(user.getId(), id));
        return new UserResult.Success();
    }
}
