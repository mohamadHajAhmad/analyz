package sdk.util.restBusinessDataValidation;

import java.util.HashMap;
import java.util.Map;

public class SuccessMessagesLocal {

    public interface Locals {

        String English = "en";

        String Spanish = "es";
    }


    /** The Constant English. */
    public static final Map<String, String> English;

    static {
        English = new HashMap<String, String>();
        English.put("1", "Test API");
        English.put("6004", "Logout Successful");
    }

    /** The Constant Spanish. */
    public static final Map<String, String> Spanish;

    static {
        Spanish = new HashMap<String, String>();
        Spanish.put("1000", "Test API");
    }
}
