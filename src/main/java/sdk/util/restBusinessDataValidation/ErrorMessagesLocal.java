package sdk.util.restBusinessDataValidation;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessagesLocal {

    public interface Locals {

        String English = "en";

        String Spanish = "es";
    }


    /** The Constant English. */
    public static final Map<String, String> English;

    static {
        English = new HashMap<String, String>();
        English.put("1", "Test API");
        English.put("1000", "Required Data Missing");
        English.put("1017", "Negative Value Invalid");
        English.put("1018", "Cannot Load More than 150 Records per Request.  Please Contact Your Administrator.");
    }

    /** The Constant Spanish. */
    public static final Map<String, String> Spanish;

    static {
        Spanish = new HashMap<String, String>();
       Spanish.put("1000", "Perdidas de Datos Necesarios");
    }

}
