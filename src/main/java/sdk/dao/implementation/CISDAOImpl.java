
package sdk.dao.implementation;


import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sdk.POJO.AbstractPojo;
import sdk.dao.AbstractDAO;
import sdk.dao.CISDAO;

import javax.sql.DataSource;
// TODO: Auto-generated Javadoc
/**
 * The Class CISDAOImpl.
 */
@SuppressWarnings("all")
@Repository
public class CISDAOImpl extends AbstractDAO implements CISDAO {

    @Autowired(required=false)
    private DataSource dataSource;
    @Autowired(required=false)
    EntityManager entityManager;

    /** The Constant LOGGER. */
    private static final Logger logger = LogManager.getLogger(CISDAOImpl.class);

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
     }



    @Override
    public void commit() {
        try {
            getSession().flush();
            getSession().clear();
            getSession().createNativeQuery("commit").executeUpdate();
        } catch (RuntimeException re) {
            logger.error("find failed", re);
            throw re;
        }
    }

    @Override
    public Session getSession() {
        return getSessionFactory().getCurrentSession();
    }


    @Override
    public boolean savePojo(AbstractPojo pojo) {
        try {
            getSession().saveOrUpdate(pojo);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePojo(AbstractPojo pojo) {
        try {
            getSession().update(pojo);
            commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePojo(AbstractPojo pojo, Boolean clearSession) {
        try {
            if (clearSession) {
                getSession().clear();
            }
            updatePojo(pojo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean savePojo(AbstractPojo pojo, Boolean clearSession) {
        try {
            if (clearSession) {
                getSession().clear();
            }
            return savePojo(pojo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mergePojo(AbstractPojo pojo, Boolean clearSession) {
        try {
            if (clearSession) {
                getSession().clear();
            }
            mergePojo(pojo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public void mergePojo(AbstractPojo pojo) {
        try {
            getSession().merge(pojo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deletePojo(AbstractPojo pojo, Boolean clearSession) {
        try {
            if (clearSession) {
                getSession().clear();
            }
            deletePojo(pojo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deletePojo(AbstractPojo pojo) {
        try {
            getSession().delete(pojo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public AbstractPojo find(Class<? extends AbstractPojo> class1, Object id) {
        try {
            return (AbstractPojo) getSession().find(class1,id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


}
