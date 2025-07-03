package Services;

import Models.Entities.User;
import Models.Enums.Role;
import Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.userRepository = repository;
        this.encoder = passwordEncoder;
    }

    public User CreateUser(String username, String password, Role role) {
        System.out.println("Creating user: " + username + " with role: " + role);
        String pwHash = encoder.encode(password);
        User user = new User(username, pwHash, role);
        return userRepository.save(user);
    }

    public User FindUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
