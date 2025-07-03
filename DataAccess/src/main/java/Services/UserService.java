package Services;

import Application.Models.Entities.User;
import DAO.HibernateUserDAO;
import Services.Interfaces.IUserService;

public class UserService implements IUserService {
    private final HibernateUserDAO UserDAO;

    public UserService(HibernateUserDAO userDAO) {
        UserDAO = userDAO;
    }

    @Override
    public User GetUser(int id) {
        return UserDAO.FindUserById(id);
    }

    @Override
    public void SaveUser(User user) {
        UserDAO.SaveUser(user);
    }

    @Override
    public void DeleteUser(User user) {
        UserDAO.DeleteUser(user);
    }
}
