package DAO.Interfaces;

import Application.Models.Entities.Operation;

import java.util.List;

public interface IOperationDAO {
    Operation GetOperationById(int id);
    void SaveOperation(Operation operation);
    void DeleteOperation(Operation operation);
    List<Operation> FindAllOperations();
    List<Operation> FindAllOperationsByAccountId(int id);
}
