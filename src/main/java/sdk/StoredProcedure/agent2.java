package sdk.StoredProcedure;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class agent2 {

    StoredProcedureQuery query;
    public agent2(Session currentSession) {
        query = currentSession.createStoredProcedureQuery("PRIME_CATALOG_PKG.ADD_PC_GROUP");

        query.registerStoredProcedureParameter("P_PARTNER_CODE", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("P_GROUP_NAME", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("P_ACCOUNT_NUMBER", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("P_USERNAME", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("P_GROUP_ID", Long.class, ParameterMode.OUT);

    }


    public Map execute(){

        Map in = new HashMap();
        query.setParameter("P_PARTNER_CODE", "cis-p-T102-0000002");
        query.setParameter("P_GROUP_NAME", "Testttt5555256419+5+656+5");
        query.setParameter("P_ACCOUNT_NUMBER", "T102-0002103");
        query.setParameter("P_USERNAME", "admin");
        query.execute();
        in.put("P_GROUP_ID",query.getOutputParameterValue("P_GROUP_ID"));
        return in;
    }
}
