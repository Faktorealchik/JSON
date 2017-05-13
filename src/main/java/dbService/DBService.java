package dbService;

import dbService.dao.LessonsDAO;
import dbService.dataSets.LessonsDataSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.SQLException;

public class DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "update"; // при смене на update кеш будет храниться при рестарте сервера||create= каждый раз новая
    private static final Logger logger = LogManager.getLogger(DBService.class.getName());

    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getMySqlConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    private Configuration getMySqlConfiguration() { //конфигурация базы данных
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(LessonsDataSet.class);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/SystemTranslating");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "root");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    public LessonsDataSet getUser(long id) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            LessonsDAO dao = new LessonsDAO(session);
            LessonsDataSet dataSet = dao.get(id);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            throw new DBException(e);
        }
    }

    public synchronized long addUser(long id, String steps, String update_date, long other) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            LessonsDAO dao = new LessonsDAO(session);
            dao.insertLesson(id, steps, update_date, other);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            throw new DBException(e);
        }
    }

    public void updateUser(long id, String steps, String update_date, long other) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            LessonsDAO dao = new LessonsDAO(session);
            dao.updateLesson(id, steps, update_date, other);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            throw new DBException(e);
        }
    }

    //printconnection
        public String printConnectInfo() {
        try {
            SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
            Connection connection = sessionFactoryImpl.getConnectionProvider().getConnection();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DB name: ").append(connection.getMetaData().getDatabaseProductName()).append("\n");
            stringBuilder.append("DB version: ").append(connection.getMetaData().getDatabaseProductVersion()).append('\n');
            stringBuilder.append("Driver: ").append(connection.getMetaData().getDriverName()).append('\n');
            stringBuilder.append("Autocommit: ").append(connection.getAutoCommit()).append('\n');
            return stringBuilder.toString();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
