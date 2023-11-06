package il.cshaifasweng.OCSFMediatorExample.server;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml.tld");
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Error configuring Hibernate", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}