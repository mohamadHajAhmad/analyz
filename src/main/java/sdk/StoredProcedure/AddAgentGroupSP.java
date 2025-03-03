package sdk.StoredProcedure;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAgentGroupSP  extends StoredProcedure{
    public static final String subProgramName = "PRIME_CATALOG_PKG.ADD_PC_GROUP";
    public AddAgentGroupSP(Session currentSession) {
        super(currentSession, subProgramName);

        declareParameter("P_PARTNER_CODE", String.class, ParameterMode.IN);
        declareParameter("P_GROUP_NAME", String.class, ParameterMode.IN);
        declareParameter("P_ACCOUNT_NUMBER", String.class, ParameterMode.IN);
        declareParameter("P_USERNAME", String.class, ParameterMode.IN);
        declareParameter("P_GROUP_ID", Long.class, ParameterMode.OUT);

    }
    public Map execute(){

        Map in = new HashMap();
        in.put("P_PARTNER_CODE", "cis-p-T102-0000002");
        in.put("P_GROUP_NAME", "Testttt6wdwad419+5+cxssdfsf656+5");
        in.put("P_ACCOUNT_NUMBER", "T102-0002103");
        in.put("P_USERNAME", "admin");
        in=super.execute(in);
        return in;
    }
}
