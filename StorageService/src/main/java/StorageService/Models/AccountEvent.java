package StorageService.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEvent {

    @Id
    @Column(name = "account_id", nullable = false, unique = true)
    private Long accountId;

    @Lob
    @Column(name = "event_data", nullable = false)
    private String eventData;
}
