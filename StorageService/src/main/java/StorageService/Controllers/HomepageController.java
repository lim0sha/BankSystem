package StorageService.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomepageController {
    @GetMapping("/")
    public String home() {
        return "StorageService is running!";
    }
}
