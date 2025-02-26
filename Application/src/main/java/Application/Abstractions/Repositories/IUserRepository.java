package Application.Abstractions.Repositories;

import Application.Contracts.ResultTypes.UserResult;
import Application.Models.Entites.User;

public interface IUserRepository {
    UserResult AddUser(User user);

    User FindUserById(Integer id);

    UserResult UpdateUser(Integer id, User userUpdate);

    UserResult DeleteUser(Integer id);
}
