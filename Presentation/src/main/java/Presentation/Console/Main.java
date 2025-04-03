package Presentation.Console;


import Presentation.Interfaces.IMenu;
import org.flywaydb.core.Flyway;

public class Main {
    public static void main(String[] args) {
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/JavaLabsDb", "postgres", "limosha").load();
        flyway.migrate();
        IMenu menu = new Menu();
        menu.Run();
    }
}
