package DAO.Interfaces;

import Application.Models.Entities.User;

import java.util.List;

public interface IUserDAO {
    void SaveUser(User user);
    void DeleteUser(User user);
    User FindUserById(int id);
    List<User> FindAllUsers();
}
