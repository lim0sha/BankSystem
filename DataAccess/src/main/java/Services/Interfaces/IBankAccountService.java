package Services.Interfaces;

import Application.Models.Entities.BankAccount;

public interface IBankAccountService {
    BankAccount GetAccount(int id);
    void UpdateAccount(BankAccount account);
    void DeleteAccount(BankAccount account);
    boolean Deposit(int accountId, double amount);
    boolean Withdraw(int accountId, double amount);
    boolean Transfer(int accountFromId, int accountToId, double amount);
}
