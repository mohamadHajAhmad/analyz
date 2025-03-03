package sdk.util.restBusinessDataValidation;

import sdk.model.ServiceExecutionResult;

public class DataValidation {

    /**
     * Check pagination lengths.
     *
     * @param start
     *            the start
     * @param length
     *            the length
     * @param result
     *            the result
     * @return the service execution result
     */
    public static ServiceExecutionResult checkPaginationLengths(Long start, Long length,
                                                                ServiceExecutionResult result) {
        if (start < 0 || length < 0) {
            result.setExecutionSuccessful(false);
            result.setErrorCode("1017");
            return result;
        }
        if (length > 150) {
            result.setExecutionSuccessful(false);
            result.setErrorCode("1018");
            return result;
        }
        return result;
    }

    /**
     * Gets the string.
     *
     * @param object
     *            the object
     * @return the string
     */
    public static String getString(Object object) {
        if (object != null) {
            String string = object.toString();
            if (!string.trim().isEmpty()) {
                return string;
            }
        }
        return null;
    }

    /**
     * Gets the boolean.
     *
     * @param object
     *            the object
     * @return the boolean
     */
    public static boolean getBoolean(Object object) {
        if (object != null) {
            boolean bool = Boolean.parseBoolean(object.toString());

            return bool;
        }
        return false;
    }

    /**
     * Checks if is empty or null.
     *
     * @param object
     *            the object
     * @return true, if is empty or null
     */
    public static boolean isEmptyOrNull(Object object) {
        return !isNotEmptyOrNull(object);
    }

    /**
     * Checks if is not empty or null.
     *
     * @param object
     *            the object
     * @return true, if is not empty or null
     */
    public static boolean isNotEmptyOrNull(Object object) {
        if (object != null) {
            String string = object.toString();
            if (!string.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
    /**
     * Result with error code.
     *
     *
     * @param errorCode
     *            the error code
     * @param result
     *            the result
     * @return the service execution result
     */
    public static ServiceExecutionResult resultWithErrorCode(String errorCode, ServiceExecutionResult result) {
        result.cleanResult();
        result.setExecutionSuccessful(false);
        result.setErrorCode(errorCode);
        return result;
    }
    public static ServiceExecutionResult checkRequiredValues(Object... arguments) {
        ServiceExecutionResult result = new ServiceExecutionResult();
        try {
            for (Object object : arguments) {
                if (object == null || object.toString().trim().isEmpty()) {
                    result.setExecutionSuccessful(false);
                    result.setErrorCode("1000");
                    return result;
                }
            }
        } catch (Exception e) {
            result.setExecutionSuccessful(false);
            result.setErrorCode("1000");
            return result;
        }
        return result;
    }

}
