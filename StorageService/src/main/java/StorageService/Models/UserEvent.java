package StorageService.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEvent {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Lob
    @Column(name = "event_data", nullable = false)
    private String eventData;
}
