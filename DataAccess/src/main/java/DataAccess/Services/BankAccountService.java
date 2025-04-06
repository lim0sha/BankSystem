package DataAccess.Services;

import Application.Models.Entities.BankAccount;
import DataAccess.Repositories.IBankAccountRepository;
import DataAccess.Services.Interfaces.IBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BankAccountService implements IBankAccountService {
    private final IBankAccountRepository BankAccountRepository;

    @Autowired
    public BankAccountService(IBankAccountRepository bankAccountRepository) {
        this.BankAccountRepository = bankAccountRepository;
    }

    @Override
    public BankAccount GetAccount(int id) {
        return BankAccountRepository.findById(id).orElse(null);
    }

    @Override
    public void UpdateAccount(BankAccount account) {
        BankAccountRepository.save(account);
    }

    @Override
    public void DeleteAccount(BankAccount account) {
        BankAccountRepository.delete(account);
    }

    @Override
    @Transactional
    public boolean Deposit(int accountId, double amount) {
        Optional<BankAccount> optional = BankAccountRepository.findById(accountId);
        if (optional.isPresent()) {
            BankAccount account = optional.get();
            account.setBalance(account.getBalance() + amount);
            BankAccountRepository.save(account);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean Withdraw(int accountId, double amount) {
        Optional<BankAccount> optional = BankAccountRepository.findById(accountId);
        if (optional.isPresent()) {
            BankAccount account = optional.get();
            if (account.getBalance() >= amount) {
                account.setBalance(account.getBalance() - amount);
                BankAccountRepository.save(account);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean Transfer(int accountFromId, int accountToId, double amount) {
        Optional<BankAccount> fromOpt = BankAccountRepository.findById(accountFromId);
        Optional<BankAccount> toOpt = BankAccountRepository.findById(accountToId);

        if (fromOpt.isPresent() && toOpt.isPresent()) {
            BankAccount from = fromOpt.get();
            BankAccount to = toOpt.get();

            if (from.getBalance() >= amount) {
                from.setBalance(from.getBalance() - amount);
                to.setBalance(to.getBalance() + amount);
                BankAccountRepository.save(from);
                BankAccountRepository.save(to);
                return true;
            }
        }
        return false;
    }
}
