package StorageService.DTO;

import StorageService.Models.UserEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventDTO {
    private Long userId;
    private String eventData;

    public UserEventDTO(UserEvent event) {
        this.userId = event.getUserId();
        this.eventData = event.getEventData();
    }
}
