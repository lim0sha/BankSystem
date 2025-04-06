package DataAccess.Configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Configuration
@EnableJpaRepositories(basePackages = "DataAccess.Repositories")
@EntityScan(basePackages = "Application.Models.Entities")
public class JPAConfig {
}