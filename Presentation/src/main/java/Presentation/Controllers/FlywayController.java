package Presentation.Controllers;

import Presentation.Interfaces.IFlywayController;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

@Component
public class FlywayController implements IFlywayController {
    Flyway flyway = null;

    @Override
    public void FlywayInit() {
        String url = System.getProperty("DB_URL");
        String username = System.getProperty("DB_USERNAME");
        String password = System.getProperty("DB_PASSWORD");
        flyway = Flyway.configure().dataSource(url, username, password).load();
    }

    @Override
    public void FlywayMigrate() {
        flyway.migrate();
    }
}
