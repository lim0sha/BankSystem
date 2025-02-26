package DataAccess;

import lombok.NoArgsConstructor;
import Application.Abstractions.Repositories.IBankAccountRepository;
import Application.Contracts.ResultTypes.BankAccountResult;
import Application.Models.Entites.BankAccount;

import java.util.ArrayList;
import java.util.Objects;

@NoArgsConstructor
public class BankAccountRepository implements IBankAccountRepository {
    ArrayList<BankAccount> _bankAccounts = new ArrayList<>();

    @Override
    public BankAccountResult AddBankAccount(BankAccount account) {
        if (account == null) {
            return new BankAccountResult.BankAccountCreationError("Bank account cannot be null");
        }
        _bankAccounts.add(account);
        return new BankAccountResult.Success();
    }

    @Override
    public BankAccount FindBankAccountById(Integer id) {
        if (id < 0) {
            return null;
        }
        for (BankAccount account : _bankAccounts) {
            if (Objects.equals(account.getId(), id)) {
                return account;
            }
        }
        return null;
    }

    @Override
    public BankAccountResult UpdateBankAccountBalance(Integer id, Double balance) {
        if (id < 0 || balance < 0) {
            return new BankAccountResult.BankAccountUpdateError("Invalid update parameters");
        }

        for (BankAccount account : _bankAccounts) {
            if (Objects.equals(account.getId(), id)) {
                account.setBalance(balance);
                return new BankAccountResult.Success();
            }
        }

        return new BankAccountResult.BankAccountUpdateError("Bank account balance update failed");
    }

    @Override
    public BankAccountResult DeleteBankAccount(Integer id) {
        if (id < 0) {
            return new BankAccountResult.BankAccountDeletionError("Invalid delete parameters");
        }
        _bankAccounts.removeIf(bankAccount -> Objects.equals(bankAccount.getId(), id));
        return new BankAccountResult.Success();
    }
}
