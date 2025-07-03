package DataAccess.Services;

import Application.Models.Entities.User;
import DataAccess.Repositories.IUserRepository;
import DataAccess.Services.Interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    private final IUserRepository UserRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.UserRepository = userRepository;
    }

    @Override
    public User GetUser(int id) {
        return UserRepository.findById(id).orElse(null);
    }

    @Override
    public void SaveUser(User user) {
        UserRepository.save(user);
    }

    @Override
    public void DeleteUser(User user) {
        UserRepository.delete(user);
    }

    @Override
    public List<User> GetAllUsers() {
        return UserRepository.findAll();
    }
}
