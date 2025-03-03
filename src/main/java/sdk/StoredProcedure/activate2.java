package sdk.StoredProcedure;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.hibernate.Session;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class activate2 {

    StoredProcedureQuery query;

     public activate2(Session currentSession) {

        query = currentSession.createStoredProcedureQuery("CUSTOMER_ORDER_PKG.ACTIVATE_FULL_ORDER");
        query.registerStoredProcedureParameter("P_ORDER_ID", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("P_START_DATE", Date.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("P_END_DATE", Date.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("P_ADDHOC_FLAG", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("P_COMMENT", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("P_SOURCE", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("P_USER", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("P_SURCHARGE_FLAG", String.class, ParameterMode.IN);

    }
    public Map execute(){

        Map in = new HashMap();
        query.setParameter("P_ORDER_ID", 35965);
        query.setParameter("P_START_DATE", null);
        query.setParameter("P_END_DATE", null);
        query.setParameter("P_ADDHOC_FLAG", "N");
        query.setParameter("P_COMMENT", "N");
        query.setParameter("P_SOURCE", "MP");
        query.setParameter("P_USER", "MP");
        query.setParameter("P_SURCHARGE_FLAG", "N");
        query.execute();
        return in;
    }

}
