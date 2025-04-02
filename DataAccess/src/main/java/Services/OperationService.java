package Services;

import Application.Models.Entities.Operation;
import DAO.HibernateOperationDAO;
import Services.Interfaces.IOperationService;

import java.util.List;

public class OperationService implements IOperationService {
    private final HibernateOperationDAO OperationDAO;

    public OperationService(HibernateOperationDAO operationDAO) {
        OperationDAO = operationDAO;
    }

    @Override
    public Operation GetOperation(int id) {
        return OperationDAO.GetOperationById(id);
    }

    @Override
    public void SaveOperation(Operation operation) {
        OperationDAO.SaveOperation(operation);
    }

    @Override
    public void DeleteOperation(Operation operation) {
        OperationDAO.DeleteOperation(operation);
    }

    @Override
    public List<Operation> FindAllOperationsByAccountId(int id) {
        return OperationDAO.FindAllOperationsByAccountId(id);
    }
}
