package DAO.Interfaces;

import Application.Models.Entities.BankAccount;

public interface IBankAccountDAO {
    BankAccount GetBankAccountByID(int id);
    void SaveBankAccount(BankAccount bankAccount);
    void DeleteBankAccount(BankAccount bankAccount);
    boolean Deposit(int accountId, double amount);
    boolean Withdraw(int accountId, double amount);
    boolean Transfer(int accountFromId, int accountToId, double amount);
}
