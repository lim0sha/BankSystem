package StorageService.Services;

import StorageService.Models.AccountEvent;
import StorageService.Models.UserEvent;
import StorageService.Repositories.AccountEventRepository;
import StorageService.Repositories.UserEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    private final UserEventRepository userEventRepository;
    private final AccountEventRepository accountEventRepository;

    @Autowired
    public ConsumerService(UserEventRepository userEventRepository, AccountEventRepository accountEventRepository) {
        this.userEventRepository = userEventRepository;
        this.accountEventRepository = accountEventRepository;
    }

    public void ConsumeUserEvent(String message, org.apache.kafka.clients.consumer.ConsumerRecord<String, String> record) {
        try {
            Long userId = Long.parseLong(record.key());
            UserEvent event = new UserEvent(userId, message);
            userEventRepository.save(event);
            System.out.println("[UserEvent] Saved: " + event);
        } catch (NumberFormatException e) {
            System.err.println("Invalid userId key: " + record.key());
        }
    }

    public void ConsumeAccountEvent(String message, org.apache.kafka.clients.consumer.ConsumerRecord<String, String> record) {
        System.out.println("Received account event: " + message);
        try {
            Long accountId = Long.parseLong(record.key());
            AccountEvent event = new AccountEvent(accountId, message);
            accountEventRepository.save(event);
            System.out.println("[AccountEvent] Saved: " + event);
        } catch (NumberFormatException e) {
            System.err.println("Invalid accountId key: " + record.key());
        }
    }
}
