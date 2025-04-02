package DAO;

import Application.Models.Entities.User;
import DAO.Interfaces.IUserDAO;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateUserDAO implements IUserDAO {
    private final SessionFactory sessionFactory;

    public HibernateUserDAO(SessionFactory sf) {
        sessionFactory = sf;
    }

    @Override
    public void SaveUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не может быть null");
        }
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(user);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void DeleteUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Пользователь или его ID не могут быть null");
        }
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(user);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User FindUserById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                Hibernate.initialize(user.getBankAccounts());
                Hibernate.initialize(user.getFriends());
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> FindAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
