package DAO;

import Application.Models.Entities.Operation;
import DAO.Interfaces.IOperationDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class HibernateOperationDAO implements IOperationDAO {
    private final SessionFactory sessionFactory;

    public HibernateOperationDAO(SessionFactory sf) {
        sessionFactory = sf;
    }

    @Override
    public Operation GetOperationById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Operation.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void SaveOperation(Operation operation) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(operation);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void DeleteOperation(Operation operation) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.delete(operation);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Operation> FindAllOperations() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Operation", Operation.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Operation> FindAllOperationsByAccountId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Operation WHERE bankAccount.id = :accountId", Operation.class)
                    .setParameter("accountId", id)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}