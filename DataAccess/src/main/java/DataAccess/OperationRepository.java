package DataAccess;

import lombok.NoArgsConstructor;
import Application.Abstractions.Repositories.IOperationRepository;
import Application.Contracts.ResultTypes.OperationResult;
import Application.Models.Entites.Operation;

import java.util.ArrayList;

@NoArgsConstructor
public class OperationRepository implements IOperationRepository {
    private final ArrayList<Operation> operations = new ArrayList<>();

    @Override
    public OperationResult AddOperation(Operation operation) {
        if (operation == null) {
            return new OperationResult.OperationError("Operation can not be null");
        }
        operations.add(operation);
        return new OperationResult.Success();
    }

    @Override
    public ArrayList<Operation> GetOperationHistory(Integer bankAccountId) {
        return operations;
    }
}
