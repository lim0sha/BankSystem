package Services.Interfaces;

import Application.Models.Entities.User;

public interface IUserService {
    User GetUser(int id);
    void SaveUser(User user);
    void DeleteUser(User user);
}
