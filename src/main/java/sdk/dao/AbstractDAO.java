
package sdk.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * The Class AbstractDAO. generic implemintaion for the DAOs that provides
 * setters and getters for the session and the session factory.
 */
public abstract class AbstractDAO {

    /**
     * The session factory.
     */
    @Autowired(required=false)
	 SessionFactory sessionFactory;

    /**
     * The Secondary session factory.
     */
    @Autowired(required=false)
	@Qualifier("secondarySessionFactory")
	SessionFactory secondarySessionFactory;

    /**
     * Gets the session factory.
     *
     * @return the session factory
     */
    public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

    /**
     * Sets the session factory.
     *
     * @param sessionFactory the new session factory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


    /**
     * Gets secondary session factory.
     *
     * @return the secondary session factory
     */
    public SessionFactory getSecondarySessionFactory() {
		return secondarySessionFactory;
	}

    /**
     * Sets the session factory.
     *
     * @param sessionFactory the new session factory
     */
    public void setSecondarySessionFactory(SessionFactory sessionFactory) {
		this.secondarySessionFactory = sessionFactory;
	}


    /**
     * Opens a new DB session.
     *
     * @return a new session
     */
    public Session openNewSession() {
        return sessionFactory.openSession();
    }

    /**
     * Opens a new DB session.
     *
     * @return a new state-less DB session
     */
    public StatelessSession openNewStatelessSession() {
        return sessionFactory.openStatelessSession();
    }

}
