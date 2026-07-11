package ch.immi.pension.persistence;

import ch.immi.pension.persistence.model.Account;
import ch.immi.pension.persistence.model.Configuration;
import ch.immi.pension.persistence.model.Setting;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class DatabaseManager {
	private static final String URL = "jdbc:h2:./database/rebalance;AUTO_SERVER=TRUE";
	private static final String USER = "sa";
	private static final String PASSWORD = "";

	final org.hibernate.cfg.Configuration configuration;

	private final SessionFactory sessionFactory;

	private boolean isConnectionOk = true;

	private static final DatabaseManager singleton = new DatabaseManager();

	public static DatabaseManager getCurrent() {
		return singleton;
	}

	private DatabaseManager() {
		configuration = getConfiguration();
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
				configuration.getProperties()).build();
	    this.sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}
	
	public static DatabaseSession openSession() {
		return getCurrent().openSessionInternal();
	}
	
	private DatabaseSession openSessionInternal() {
		return new DatabaseSession(this.sessionFactory.openSession());
	}

	public void close() {
		if (sessionFactory != null && !sessionFactory.isClosed()) {
			sessionFactory.close();
		}
	}

	private static org.hibernate.cfg.Configuration getConfiguration() {
			return new org.hibernate.cfg.Configuration()
					.setProperty("hibernate.connection.url", URL)
					.setProperty("hibernate.connection.username", USER)
					.setProperty("hibernate.connection.password", PASSWORD)
					.setProperty("hibernate.driver_class", "org.h2.Driver")
					// Automatically creates/updates your H2 tables matching your entities!
					.setProperty("hibernate.hbm2ddl.auto", "update")
					.setProperty("hibernate.show_sql", "true")
					.addAnnotatedClass(Setting.class)
					.addAnnotatedClass(Account.class)
					.addAnnotatedClass(Configuration.class);
	}

	/**
	 * Checks internet connection to database.
	 */
	public boolean isConnectionOk() {
		if (isConnectionOk) {
			try {
				Session session = getCurrent().sessionFactory.openSession();
				session.getTransaction().begin();
				session.getTransaction().commit();
			} catch (Throwable t) {
				isConnectionOk = false;
			}
		}
		return isConnectionOk;
	}
}
