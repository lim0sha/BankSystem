package Application.Abstractions.Repositories;

import Application.Contracts.ResultTypes.OperationResult;
import Application.Models.Entites.Operation;

import java.util.ArrayList;

public interface IOperationRepository {
    OperationResult AddOperation(Operation operation);

    ArrayList<Operation> GetOperationHistory(Integer bankAccountId);
}
