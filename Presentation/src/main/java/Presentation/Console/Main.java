package Presentation.Console;

import Presentation.Configs.DotenvInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.addInitializers(new DotenvInitializer());
        app.run(args);
    }
}
