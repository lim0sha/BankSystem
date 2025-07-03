package Services.Interfaces;

import Application.Models.Entities.Operation;

import java.util.List;

public interface IOperationService {
    Operation GetOperation(int id);
    void SaveOperation(Operation operation);
    void DeleteOperation(Operation operation);
    List<Operation> FindAllOperationsByAccountId(int id);
}
