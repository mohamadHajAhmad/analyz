
package sdk.dao;

import org.hibernate.Session;
import sdk.POJO.AbstractPojo;

import javax.sql.DataSource;


// TODO: Auto-generated Javadoc

/**
 * The Interface CISDAO.
 */
@SuppressWarnings("all")
public interface CISDAO {

    /**
     * Gets the data source.
     *
     * @return the data source
     */
    public DataSource getDataSource();

    /**
     * Sets the data source.
     *
     * @param dataSource
     *            the new data source
     */
    public void setDataSource(DataSource dataSource);

    /**
     * Commit.
     */
    public void commit();

    /**
     * Gets the session.
     *
     * @return the session
     */
    public Session getSession();


    /**
     * Save pojo.
     *
     * @param pojo
     *            the pojo
     * @return true, if successful
     */
    public boolean savePojo(AbstractPojo pojo);

    /**
     * Update pojo.
     *
     * @param pojo
     *            the pojo
     */
    public void updatePojo(AbstractPojo pojo);

    /**
     * Update pojo.
     *
     * @param pojo
     *            the pojo
     * @param clearSession
     *            the clear session
     */
    public void updatePojo(AbstractPojo pojo, Boolean clearSession);

    /**
     * Save pojo.
     *
     * @param pojo
     *            the pojo
     * @param clearSession
     *            the clear session
     * @return true, if successful
     */
    public boolean savePojo(AbstractPojo pojo, Boolean clearSession);


    /**
     * Merge pojo.
     *
     * @param pojo
     *            the pojo
     * @param clearSession
     *            the clear session
     */
    public void mergePojo(AbstractPojo pojo, Boolean clearSession);

    /**
     * Merge pojo.
     *
     * @param pojo
     *            the pojo
     */
    public void mergePojo(AbstractPojo pojo);

    /**
     * Delete pojo.
     *
     * @param pojo
     *            the pojo
     * @param clearSession
     *            the clear session
     */
    public void deletePojo(AbstractPojo pojo, Boolean clearSession);

    /**
     * Delete pojo.
     *
     * @param pojo
     *            the pojo
     */
    public void deletePojo(AbstractPojo pojo);

    public AbstractPojo find(Class<? extends AbstractPojo> class1, Object id);


}
