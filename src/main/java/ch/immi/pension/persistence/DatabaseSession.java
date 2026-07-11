package ch.immi.pension.persistence;

import ch.immi.pension.exception.DatabaseException;
import jakarta.persistence.criteria.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseSession {
	private final Session session;

	private int transactionCounter = 0;

	private Transaction currentTransaction = null;

	public static class Param<T> {
		String name;
		String value;

		public Param(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public static <T> Param<T> of(String name, String value) {
			return new Param<>(name, value);
		}

		public Predicate getPredicate(CriteriaBuilder cb, Root<T> root) {
			return cb.equal(root.get(this.name), this.value);
		}
	}
	
	public DatabaseSession(final Session session) {
		super();
		this.session = session;
	}

	public synchronized void beginTransaction() throws HibernateException {
		if (transactionCounter == 0 || currentTransaction == null) {
			currentTransaction = this.session.beginTransaction();
			transactionCounter = 0;
		}
		transactionCounter++;
	}

	public synchronized void rollbackTransaction() throws HibernateException {
		if (transactionCounter > 0 && session != null && currentTransaction != null) {
			try {
				currentTransaction.rollback();
			} finally {
				transactionCounter = 0;
			}
		}
	}

	public synchronized void commitTransaction() throws HibernateException {
		transactionCounter--;
		if (transactionCounter <= 0 && session != null && currentTransaction != null) {
			try {
				currentTransaction.commit();
			} finally {
				transactionCounter = 0;
			}
		}
	}

	/**
	 * Returns primary Key
	 */
	public <T> void insert(final T obj) throws DatabaseException {
		beginTransaction();
		boolean saveOk = false;
		try {
			this.session.persist(obj);
			saveOk = true;
		} catch(Exception e) {
			throw new DatabaseException("Could not insert " + obj, e);
		} finally {
			if (saveOk) {
				commitTransaction();
			} else {
				rollbackTransaction();
			}
		}
	}

	public void update(Object obj) throws DatabaseException {
		beginTransaction();
		boolean updateOk = false;
		try {
			obj = this.session.merge(obj);
			updateOk = true;
		} catch(Exception e) {
			throw new DatabaseException("Could not update " + obj, e);
		} finally {
			if (updateOk) {
				commitTransaction();
			} else {
				rollbackTransaction();
			}
		}
	}

	public void delete(final Object obj) throws DatabaseException {
		beginTransaction();
		boolean deleteOk = false;
		try {
			this.session.remove(this.session.merge(obj));
			deleteOk = true;
		} catch(Exception e) {
			throw new DatabaseException("Could not delete " + obj, e);
		} finally {
			if (deleteOk) {
				commitTransaction();
			} else {
				rollbackTransaction();
			}
		}
	}

	public <T> List<T> read(final Class<T> clazz, final List<String> orderStringList, final List<Param<T>> paramList) {
		CriteriaBuilder cb = session.getCriteriaBuilder();

		CriteriaQuery<T> query = cb.createQuery(clazz);

		Root<T> root = query.from(clazz);

		// 4. SELECT- und WHERE-Klausel definieren (Name UND Geburtsdatum müssen übereinstimmen)
		if (paramList != null) {
			query.select(root).where(
					cb.and(paramList.stream().map(p -> p.getPredicate(cb, root)).toList())
			);
		}

		// 5. Sortierung festlegen (optional, z.B. nach ID aufsteigend)
		List<Order> orderList = new ArrayList<>();
		if (orderStringList != null) {
			orderList = orderStringList.stream().map(s -> cb.asc(root.get("id"))).toList();
		}

		// 1. Apply the sorting orders if the list is provided and not empty
		if (!orderList.isEmpty()) {
			query.orderBy(orderList);
		}

		// 2. Create the executable query from the session and fetch results
		return this.session.createQuery(query).getResultList();
	}

	public <T> Optional<T> readByPK(final Class<T> clazz, final Long pk) {
		beginTransaction();
		boolean readOk = false;
		try {
			T object = (T) this.session.find(clazz, pk);
			readOk = true;
			if (object != null) {
				return Optional.of(object);
			}
		} catch(Exception e) {
			System.err.println("Error readByPK: " + e.getMessage());
		} finally {
			if (readOk) {
				commitTransaction();
			} else {
				rollbackTransaction();
			}
		}
		return Optional.empty();
	}

	public synchronized <T> List<T> readAll(final Class<T> clazz, final List<String> orderStringList) {
		return read(clazz, orderStringList, null);
	}

	public boolean isConnected() {
		return this.session.isConnected();
	}
}
