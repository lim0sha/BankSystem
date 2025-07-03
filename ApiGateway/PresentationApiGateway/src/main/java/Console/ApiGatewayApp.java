package Console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "Controllers",
        "Configs",
        "JWT",
        "Services",
        "Repositories",
        "Models.Entities",
        "Models.Enums"
})
@EnableJpaRepositories(basePackages = "Repositories")
@EntityScan(basePackages = "Models.Entities")
public class ApiGatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApp.class, args);
    }
}
