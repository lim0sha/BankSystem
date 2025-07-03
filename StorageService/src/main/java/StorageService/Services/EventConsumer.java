package StorageService.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumer {

    private final ConsumerService consumerService;

    @Autowired
    public EventConsumer(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @KafkaListener(topics = "client-topic", groupId = "my-group")
    public void consumeUserEvent(String message, org.apache.kafka.clients.consumer.ConsumerRecord<String, String> record) {
        consumerService.ConsumeUserEvent(message, record);
    }

    @KafkaListener(topics = "account-topic", groupId = "my-group")
    public void consumeAccountEvent(String message, org.apache.kafka.clients.consumer.ConsumerRecord<String, String> record) {
        consumerService.ConsumeAccountEvent(message, record);
    }
}
