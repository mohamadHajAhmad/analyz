package sdk.StoredProcedure;


import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.hibernate.Session;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ActivateFullOrderSP extends StoredProcedure{

    public static final String subProgramName = "CUSTOMER_ORDER_PKG.ACTIVATE_FULL_ORDER";


    public ActivateFullOrderSP(Session currentSession) {
        super(currentSession, subProgramName);

        declareParameter("P_ORDER_ID", Long.class, ParameterMode.IN);
        declareParameter("P_START_DATE", Date.class, ParameterMode.IN);
        declareParameter("P_END_DATE", Date.class, ParameterMode.IN);
        declareParameter("P_ADDHOC_FLAG", String.class, ParameterMode.IN);
        declareParameter("P_COMMENT", String.class, ParameterMode.IN);
        declareParameter("P_SOURCE", String.class, ParameterMode.IN);
        declareParameter("P_USER", String.class, ParameterMode.IN);
        declareParameter("P_SURCHARGE_FLAG", String.class, ParameterMode.IN);


    }


    public Map execute(){

        Map in = new HashMap();
        in.put("P_ORDER_ID", 36465);
        in.put("P_START_DATE", null);
        in.put("P_END_DATE", null);
        in.put("P_ADDHOC_FLAG", "N");
        in.put("P_COMMENT", "N");
        in.put("P_SOURCE", "MP");
        in.put("P_USER", "MP");
        in.put("P_SURCHARGE_FLAG", "N");
        in=super.execute(in);
         return in;
    }

}
