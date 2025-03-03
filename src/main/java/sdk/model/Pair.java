package sdk.model;

/**
 * Represent pair of key and value
 */
public class Pair {
    private String name ;
    private Object value;

    /**
     * create instance of Pair with name and value
     * @param name
     * @param value
     */
    public Pair(String name, Object value) {
        setName(name);
        setValue(value);
    }

    /**
     *
     * @param name
     */
    private void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param value
     */

    private void setValue(Object value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     *
     * @return
     */
    public String getStringValue() {
        return value.toString();
    }
    /**
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }




    /**
     * check ??
     * @param arg
     * @return false if arg value is null or empty and true otherwise
     */
    private boolean isValidString(String arg) {
        if (arg == null || arg.trim().isEmpty()) return false;
        return true;
    }
}
