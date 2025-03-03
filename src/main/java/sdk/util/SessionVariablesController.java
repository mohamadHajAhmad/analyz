package sdk.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SessionVariablesController {

    /** The Constant LOGGER. */
    private static final Logger logger = LogManager.getLogger(SessionVariablesController.class);

    /** The session variables. */
    private static Map<String,Object> sessionVariables = new HashMap<String, Object>();

    /**
     * Instantiates a new session variables controller.
     */
    public SessionVariablesController() {

    }


    /**
     * Gets the current request attributes.
     *
     * @param key
     *            the key
     * @return the current request attributes
     */
    public static Object getCurrentRequestAttribute(String key) {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getAttribute(key);
    }


    /**
     * Gets the current user group.
     *
     * @return the current user group
     */
    public static boolean isHttpsUrl() {
        Object value = getSessionVariable(SystemConstants.IS_HTTPS);
        if (value != null) {
            return (boolean) value;
        }
        return false;
    }



    /**
     * Gets the session variable.
     *
     * @param key the key
     * @return the session variable
     */
    public static Object getSessionVariable(String key) {
        return getCurrentRequestAttribute(key);
    }


    /**
     * Gets the session variables.
     *
     * @return the session variables
     */
    public static Map<String, Object> getSessionVariables() {
        Map<String, Object> data = new HashMap<>();

        Enumeration<String> keys = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getAttributeNames();
        while(keys.hasMoreElements()) {
            String key = keys.nextElement();
            if(!key.contains("."))
                data.put(key, ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getAttribute(key));
        }
        return data;
    }

    /**
     * Sets the session variables.
     *
     * @param sessionVariablesR the session variables R
     */
    public static void setSessionVariables(Map<String, Object> sessionVariablesR) {
        sessionVariables = sessionVariablesR;
    }

    /**
     * Adds the session variable.
     *
     * @param key the key
     * @param value the value
     */
    public static void addSessionVariable(String key, Object value) {
        /* add one variable */
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().setAttribute(key, value);
    }

    /**
     * Gets the current user Local.
     *
     * @return the current user Local
     */
    public static String getCurrentUserLocal() {
        Object value = getSessionVariable(SystemConstants.CURRENT_ACTIVE_USER_LOCAL);
        if (value != null) {
            return value.toString();
        }
        // LOGGER.error("Local Was not found! ");
        return "en";
    }

}
