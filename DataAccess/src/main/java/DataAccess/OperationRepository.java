package DataAccess;

import lombok.NoArgsConstructor;
import Application.Abstractions.Repositories.IOperationRepository;
import Application.Contracts.ResultTypes.OperationResult;
import Application.Models.Entites.Operation;

import java.util.ArrayList;

/**
 * Репозиторий для управления операциями банковских счетов.
 * Реализует интерфейс {@link IOperationRepository} и выполняет операции добавления и получения истории операций.
 *
 * <p>Хранение операций осуществляется в памяти с использованием {@link ArrayList}.</p>
 */
@NoArgsConstructor
public class OperationRepository implements IOperationRepository {

    /**
     * Внутреннее хранилище операций в памяти.
     */
    private final ArrayList<Operation> operations = new ArrayList<>();

    /**
     * Добавляет новую операцию в хранилище.
     *
     * @param operation объект операции, который будет добавлен.
     * @return результат операции добавления: {@link OperationResult.Success} при успешном добавлении
     * или {@link OperationResult.OperationError} в случае ошибки.
     */
    @Override
    public OperationResult AddOperation(Operation operation) {
        if (operation == null) {
            return new OperationResult.OperationError("Operation can not be null");
        }
        operations.add(operation);
        return new OperationResult.Success();
    }

    /**
     * Возвращает историю операций для указанного идентификатора банковского счета.
     *
     * @param bankAccountId идентификатор банковского счета, для которого требуется история операций.
     * @return список операций {@link ArrayList} связанных с банковским счетом.
     */
    @Override
    public ArrayList<Operation> GetOperationHistory(Integer bankAccountId) {
        return operations;
    }
}
