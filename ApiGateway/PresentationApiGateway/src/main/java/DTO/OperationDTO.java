package DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OperationDTO {
    private Integer id;
    private String type;
    private Double amount;
    private Integer accountId;
}
