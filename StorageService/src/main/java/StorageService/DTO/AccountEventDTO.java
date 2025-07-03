package StorageService.DTO;

import StorageService.Models.AccountEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountEventDTO {
    private Long accountId;
    private String eventData;

    public AccountEventDTO(AccountEvent event) {
        this.accountId = event.getAccountId();
        this.eventData = event.getEventData();
    }
}
