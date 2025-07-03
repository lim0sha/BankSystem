package Presentation.Configs;

import Presentation.Interfaces.IMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppInitializer implements CommandLineRunner {

    private final IMenu menu;

    @Autowired
    public AppInitializer(IMenu menu) {
        this.menu = menu;
    }

    @Override
    public void run(String... args) {
        // menu.Run();
    }
}