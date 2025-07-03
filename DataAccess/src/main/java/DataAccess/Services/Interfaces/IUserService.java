package DataAccess.Services.Interfaces;

import Application.Models.Entities.User;

import java.util.List;

public interface IUserService {
    User GetUser(int id);

    void SaveUser(User user);

    void DeleteUser(User user);

    List<User> GetAllUsers();
}
