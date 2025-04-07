package Presentation.DTO;

import Application.Models.Entities.Operation;
import Application.Models.Enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OperationDTO {
    private final Integer id;
    private final OperationType type;
    private final Double amount;
    private final Integer accountId;

    public OperationDTO(Operation operation) {
        this.id = operation.getId();
        this.amount = operation.getAmount();
        this.type = OperationType.valueOf(operation.getType());
        this.accountId = operation.getBankAccount().getId();
    }
}
