package StorageService.Services;

import StorageService.Models.UserEvent;
import StorageService.Models.AccountEvent;
import StorageService.Repositories.UserEventRepository;
import StorageService.Repositories.AccountEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumerService {

    private final UserEventRepository userEventRepository;
    private final AccountEventRepository accountEventRepository;

    @Autowired
    public EventConsumerService(UserEventRepository userEventRepository, AccountEventRepository accountEventRepository) {
        this.userEventRepository = userEventRepository;
        this.accountEventRepository = accountEventRepository;
    }

    @KafkaListener(topics = "client-topic", groupId = "my-group")
    public void consumeUserEvent(String message, org.apache.kafka.clients.consumer.ConsumerRecord<String, String> record) {
        try {
            Long userId = Long.parseLong(record.key());
            UserEvent event = new UserEvent(userId, message);
            userEventRepository.save(event);
            System.out.println("[UserEvent] Saved: " + event);
        } catch (NumberFormatException e) {
            System.err.println("Invalid userId key: " + record.key());
        }
    }

    @KafkaListener(topics = "account-topic", groupId = "my-group")
    public void consumeAccountEvent(String message, org.apache.kafka.clients.consumer.ConsumerRecord<String, String> record) {
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
