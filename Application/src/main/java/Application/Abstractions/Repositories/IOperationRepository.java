package Application.Abstractions.Repositories;

import Application.Contracts.ResultTypes.OperationResult;
import Application.Models.Entites.Operation;

import java.util.ArrayList;

/**
 * Репозиторий для управления операциями с банковскими счетами.
 * Реализует паттерн CRUD для добавления операций и получения истории операций.
 */
public interface IOperationRepository {

    /**
     * Добавляет новую операцию в хранилище данных.
     *
     * @param operation операция, которую необходимо добавить в хранилище.
     * @return результат операции добавления, может содержать сообщение об ошибке или подтверждение успеха.
     */
    OperationResult AddOperation(Operation operation);

    /**
     * Извлекает историю операций для конкретного банковского счета.
     *
     * @param bankAccountId идентификатор банковского счета, для которого нужно получить историю операций.
     * @return список операций, выполненных с указанным банковским счетом.
     */
    ArrayList<Operation> GetOperationHistory(Integer bankAccountId);
}
