package Presentation.Configs;

import Presentation.Interfaces.IFlywayController;
import Presentation.Interfaces.IMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppInitializer implements CommandLineRunner {

    private final IFlywayController flywayController;
    private final IMenu menu;

    @Autowired
    public AppInitializer(IFlywayController flywayController, IMenu menu) {
        this.flywayController = flywayController;
        this.menu = menu;
    }

    @Override
    public void run(String... args) {
        flywayController.FlywayInit();
        flywayController.FlywayMigrate();
        menu.Run();
    }
}