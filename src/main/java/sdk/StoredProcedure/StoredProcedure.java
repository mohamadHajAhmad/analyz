package sdk.StoredProcedure;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StoredProcedure {
    StoredProcedureQuery storedProcedureQuery;

    List<String> outParam=new ArrayList<>();


    protected StoredProcedure(Session currentSession, String name) {
        this.storedProcedureQuery=currentSession.createStoredProcedureQuery(name);
    }

    public Map execute(Map<String, ?> inParams) {

        Map in = new HashMap();
        for (Map.Entry<String,?> entry : inParams.entrySet())
            storedProcedureQuery.setParameter(entry.getKey(),entry.getValue());
         storedProcedureQuery.execute();
         if (outParam!=null && !outParam.isEmpty()) {
              for (String param :outParam)
                  in.put(param,storedProcedureQuery.getOutputParameterValue(param));
         }
         else
             in.put("Success",true);
        return in;
    }

    public Boolean execute(Object... inParams) {
        return storedProcedureQuery.execute();
    }


    /**
     * Declare a parameter. Overridden method.
     * Parameters declared as {@code InParamter} and {@code InOutParameter}
     * will always be used to provide input values.  In addition to this any parameter declared
     * <b>Note: Calls to declareParameter must be made in the same order as
     * they appear in the database's stored procedure parameter list.</b>
     * Names are purely used to help mapping.
     */

    public void declareParameter(String var1, Class var2, ParameterMode var3) {
        if(var3!=null && (var3.name().equals("OUT")| var3.name().equals("INOUT")) ) {
            outParam.add(var1);
        }
        storedProcedureQuery.registerStoredProcedureParameter(var1, var2, var3);
    }

}
