package Presentation.Configs;

import Application.Managers.IUserManager;
import Presentation.Console.Menu;
import DataAccess.Services.Interfaces.IBankAccountService;
import DataAccess.Services.Interfaces.IOperationService;
import DataAccess.Services.Interfaces.IUserService;
import Presentation.Interfaces.IBaseController;
import Presentation.Controllers.BaseController;
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
    public BaseController baseController(
            IUserManager userManager,
            IUserService userService,
            IBankAccountService bankAccountService,
            IOperationService operationService) {
        return new BaseController(userManager, userService, bankAccountService, operationService);
    }

    @Bean
    public Menu menu(IBaseController baseController, Scanner scanner) {
        return new Menu(baseController, scanner);
    }
}