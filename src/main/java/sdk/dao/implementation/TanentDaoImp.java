package sdk.dao.implementation;

import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import sdk.dao.TanentDao;

@Repository
public class TanentDaoImp extends CISDAOImpl implements TanentDao {
    @Override
    @Transactional
    public String getPartner(String partnerCode) {
        Session currentSession = getSessionFactory().getCurrentSession();
        String sql = "SELECT partner_code FROM Partners where partner_code=:partnerCode";
        Query query = currentSession.createNativeQuery(sql,String.class).setParameter("partnerCode",partnerCode);
        Object result = query.uniqueResult();
        if (result == null)
            return null;
        String data=query.toString();
        return data;
    }
}
