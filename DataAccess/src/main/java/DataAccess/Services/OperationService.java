package DataAccess.Services;

import Application.Models.Entities.Operation;
import DataAccess.Repositories.IOperationRepository;
import DataAccess.Services.Interfaces.IOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationService implements IOperationService {
    private final IOperationRepository OperationRepository;

    @Autowired
    public OperationService(IOperationRepository operationRepository) {
        OperationRepository = operationRepository;
    }

    @Override
    public Operation GetOperation(int id) {
        return OperationRepository.findById(id).orElse(null);
    }

    @Override
    public void SaveOperation(Operation operation) {
        OperationRepository.save(operation);
    }

    @Override
    public void DeleteOperation(Operation operation) {
        OperationRepository.delete(operation);
    }

    @Override
    public List<Operation> FindAllOperationsByAccountId(int id) {
        return OperationRepository.findAllByBankAccount_Id(id);
    }

    @Override
    public List<Operation> FindAllOperations() {
        return OperationRepository.findAll();
    }
}
