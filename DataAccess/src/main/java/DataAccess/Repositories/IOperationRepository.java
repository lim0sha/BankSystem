package DataAccess.Repositories;

import Application.Models.Entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOperationRepository extends JpaRepository<Operation, Integer> {
    List<Operation> findAllByBankAccount_Id(int id);
}
