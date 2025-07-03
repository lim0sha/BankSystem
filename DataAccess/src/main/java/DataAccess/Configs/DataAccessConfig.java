package DataAccess.Configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "DataAccess.Services")
public class DataAccessConfig {
}