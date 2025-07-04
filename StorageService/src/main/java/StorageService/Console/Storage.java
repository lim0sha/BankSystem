package StorageService.Console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class Storage {
    public static void main(String[] args) {
        SpringApplication.run(Storage.class, args);
    }
}
