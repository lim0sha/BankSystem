package Utils;

import Application.Models.Entities.BankAccount;
import Application.Models.Entities.Operation;
import Application.Models.Entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class HibernateSessionFactoryUtil {
    private static SessionFactory SessionFactory;

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory GetSessionFactory() {
        if (SessionFactory == null) {
            try {
                Configuration config = new Configuration().configure();

                config.addAnnotatedClass(User.class);
                config.addAnnotatedClass(BankAccount.class);
                config.addAnnotatedClass(Operation.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(config.getProperties());

                SessionFactory = config.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.out.println("Error in getSessionFactory: " + e.getMessage());
                throw new RuntimeException("Failed to create sessionFactory", e);
            }
        }
        return SessionFactory;
    }
}
