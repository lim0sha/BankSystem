package DataAccess;

import lombok.NoArgsConstructor;
import Application.Abstractions.Repositories.IUserRepository;
import Application.Contracts.ResultTypes.UserResult;
import Application.Models.Entites.User;

import java.util.ArrayList;
import java.util.Objects;

@NoArgsConstructor
public class UserRepository implements IUserRepository {

    private final ArrayList<User> _users = new ArrayList<>();

    @Override
    public UserResult AddUser(User user) {
        if (user == null) {
            return new UserResult.UserCreationError("User can not be null");
        }
        _users.add(user);
        return new UserResult.Success();
    }

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


    @Override
    public UserResult DeleteUser(Integer id) {
        if (id < 0) {
            return new UserResult.UserDeletionError("Invalid delete parameters");
        }
        _users.removeIf(user -> Objects.equals(user.getId(), id));
        return new UserResult.Success();
    }
}
