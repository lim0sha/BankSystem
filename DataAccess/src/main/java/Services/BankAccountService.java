package Services;

import Application.Models.Entities.BankAccount;
import DAO.HibernateBankAccountDAO;
import Services.Interfaces.IBankAccountService;

public class BankAccountService implements IBankAccountService {
    private final HibernateBankAccountDAO BankAccountDAO;

    public BankAccountService(HibernateBankAccountDAO bankAccountDAO) {
        BankAccountDAO = bankAccountDAO;
    }
    @Override
    public BankAccount GetAccount(int id) {
        return BankAccountDAO.GetBankAccountByID(id);
    }

    @Override
    public void UpdateAccount(BankAccount account) {
        BankAccountDAO.SaveBankAccount(account);
    }

    @Override
    public void DeleteAccount(BankAccount account) {
        BankAccountDAO.DeleteBankAccount(account);
    }

    @Override
    public boolean Deposit(int accountId, double amount) {
        return BankAccountDAO.Deposit(accountId, amount);
    }

    @Override
    public boolean Withdraw(int accountId, double amount) {
        return BankAccountDAO.Withdraw(accountId, amount);
    }

    @Override
    public boolean Transfer(int accountFromId, int accountToId, double amount) {
        return BankAccountDAO.Transfer(accountFromId, accountToId, amount);
    }
}
