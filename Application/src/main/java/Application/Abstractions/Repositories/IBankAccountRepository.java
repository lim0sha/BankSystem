package Application.Abstractions.Repositories;

import Application.Contracts.ResultTypes.BankAccountResult;
import Application.Models.Entites.BankAccount;

public interface IBankAccountRepository {
    BankAccountResult AddBankAccount(BankAccount account);

    BankAccount FindBankAccountById(Integer id);

    BankAccountResult UpdateBankAccountBalance(Integer id, Double balance);

    BankAccountResult DeleteBankAccount(Integer id);
}
