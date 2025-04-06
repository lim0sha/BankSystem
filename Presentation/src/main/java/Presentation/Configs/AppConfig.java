package Presentation.Configs;

import Application.Managers.UserManager;
import Presentation.Console.Menu;
import Presentation.Controllers.UserController;
import DataAccess.Services.Interfaces.IBankAccountService;
import DataAccess.Services.Interfaces.IOperationService;
import DataAccess.Services.Interfaces.IUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = { "Presentation", "DataAccess", "Application" })
public class AppConfig {

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public UserController userController(
            UserManager userManager,
            IUserService userService,
            IBankAccountService bankAccountService,
            IOperationService operationService) {
        return new UserController(userManager, userService, bankAccountService, operationService);
    }

    @Bean
    public Menu menu(UserController userController, Scanner scanner) {
        return new Menu(userController, scanner);
    }
}