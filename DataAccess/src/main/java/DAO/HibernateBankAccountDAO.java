package DAO;

import Application.Models.Entities.BankAccount;
import DAO.Interfaces.IBankAccountDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateBankAccountDAO implements IBankAccountDAO {
    private final SessionFactory sessionFactory;

    public HibernateBankAccountDAO(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public BankAccount GetBankAccountByID(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(BankAccount.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void SaveBankAccount(BankAccount account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.saveOrUpdate(account);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                e.printStackTrace();
            }
        }
    }

    public void DeleteBankAccount(BankAccount bankAccount) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(bankAccount);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean Deposit(int accountId, double amount) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            BankAccount account = session.get(BankAccount.class, accountId);
            if (account != null) {
                account.setBalance(account.getBalance() + amount);
                session.saveOrUpdate(account);
                tx.commit();
                return true;
            }
            tx.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean Withdraw(int accountId, double amount) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            BankAccount account = session.get(BankAccount.class, accountId);
            if (account != null && account.getBalance() >= amount) {
                account.setBalance(account.getBalance() - amount);
                session.saveOrUpdate(account);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean Transfer(int accountFromId, int accountToId, double amount) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            BankAccount accountFrom = session.get(BankAccount.class, accountFromId);
            BankAccount accountTo = session.get(BankAccount.class, accountToId);

            if (accountFrom != null && accountTo != null && accountFrom.getBalance() >= amount) {
                accountFrom.setBalance(accountFrom.getBalance() - amount);
                accountTo.setBalance(accountTo.getBalance() + amount);
                session.saveOrUpdate(accountFrom);
                session.saveOrUpdate(accountTo);
                tx.commit();
                return true;
            }

            tx.rollback();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
