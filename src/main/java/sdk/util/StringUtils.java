package sdk.util;

import java.util.Base64;

public class StringUtils {


    /**
     *
     * @param string
     * @return encoded string
     */
    public static String encodeString(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes());
    }

}
