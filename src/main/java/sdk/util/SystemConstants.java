package sdk.util;


import java.util.Arrays;
import java.util.List;

public interface SystemConstants {


    /** The bpm current active user name. */
    String CURRENT_ACTIVE_USER_NAME = "current_active_user_name";

    /** The bpm current active user group. */
    String CURRENT_ACTIVE_USER_GROUP = "current_active_user_group";

    /** The bpm current active user host. */
    String CURRENT_ACTIVE_USER_HOST = "current_active_user_host";

    /** The bpm current active user agent. */
    String CURRENT_ACTIVE_USER_AGENT = "current_active_user_agent";

    /** The bpm current active user session id. */
    String CURRENT_ACTIVE_USER_SESSION = "current_active_user_session";

    /** The bpm current active user group. */
    String IS_HTTPS = "is_https";

    /** The current active user local. */
    String CURRENT_ACTIVE_USER_LOCAL = "current_active_user_local";


    public interface NotificationLog {
        /** The request. */
        String REQUEST = "REQUEST";

        /** The response. */
        String RESPONSE = "RESPONSE";


    }

    /** The like types. */
    List<String> likeTypes = Arrays.asList("lkf", "lkl", "lkb", "ilkb");

    /**
     * The Interface Authorization.
     */
    public interface Authorization {

        /** The basic. */
        String BASIC = "Basic";

        /** The bearer. */
        String BEARER = "bearer";

    }

    public interface HttpMethods {
        String GET = "GET";
        String POST = "POST";
        String PUT = "PUT";
        String DELETE = "DELETE";

    }

    /**
     * Http request body types
     */
    public interface HttpBodyTypes {
        String FILE = "FILE";
        String STRING = "STRING";
        String BYTES = "BYTE";
        String INPUT_STREAM="INPUT_STREAM";


    }
    /**
     * java variables types
     */
    public interface Types {
        String BYTE = "java.lang.Byte";
        String INPUT_STREAM = "java.io.InputStream";
        String FILE = "java.io.File";

    }
}
