package sdk.dao.util;

import sdk.model.AbstractModel;

import java.util.LinkedHashMap;

public class QueryConditionModel extends AbstractModel implements java.io.Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The variable. */
    private String variable;

    /** The type. */
    private String type;

    /** The operator. */
    private String operator;

    /** The condition. */
    private String condition;

    /** The value. */
    private Object value;

    /** The key. */
    private String key;

    /** The field. */
    private String field;


    public enum Joins {
        inner("INNER"),
        left("LEFT"),
        right("RIGHT");


        private String code;
        Joins(String code) {
            this.code = code;
        }
        public String code() {
            return code;
        }
    }
    /**
     * The Interface conditions.
     */
    public enum Conditions {

        /** The and. */
        AND("and"),

        /** The or. */
        OR("or"),

        /** The none. */
        NONE("none");

        /** The code. */
        private String code;

        /**
         * Instantiates a new conditions.
         *
         * @param code
         *            the code
         */
        Conditions(String code) {
            this.code = code;
        }

        /**
         * Code.
         *
         * @return the string
         */
        public String code() {
            return code;
        }
    }

    /**
     * The Interface operators.
     */
    public enum Operators {

        /** The equal. */
        EQUAL("eq", "${field} = '${variable}'"),

        /** The not equal. */
        NOT_EQUAL("neq", "${field} <> '${variable}'"),

        /** The like first. */
        LIKE_FIRST("lkf", "${field} like '%${variable}'"),

        /** The like last. */
        LIKE_LAST("lkl", "${field} like '${variable}%'"),

        /** The like both. */
        LIKE_BOTH("lkb", "${field} like '%${variable}%'"),

        /** The like both  case not sensitive. */
        LIKE_BOTH_INSENSITIVE("ilkb", "${field} like '%${variable}%'"),

        /** The not like both  case not sensitive. */
        NOT_LIKE_BOTH_INSENSITIVE("nilkb", "${field} not like '%${variable}%'"),

        /** The less than. */
        LESS_THAN("lt", "${field} < '${variable}'"),

        /** The less than equal. */
        LESS_THAN_EQUAL("lte", "${field} <= '${variable}'"),

        /** The greater than. */
        GREATER_THAN("gt", "${field} > '${variable}'"),

        /** The greater than equal. */
        GREATER_THAN_EQUAL("gte", "${field} >= '${variable}'"),

        /** The is null. */
        IS_NULL("nil", "${field} is null"),

        /** The is not null. */
        IS_NOT_NULL("nnil", "${field} is not null"),

        /** The is empty. */
        IS_EMPTY("em", "${field} = ''"),

        /** The is not empty. */
        IS_NOT_EMPTY("nem", "${field} <> ''"),

        /** The between. */
        BETWEEN("btwn", "${field} between '${variable1} and ${variable2}'"),

        /** The not between. */
        NOT_BETWEEN("nbtwn", "${field} not between '${variable1} and ${variable2}'"),
        /** The in. */
        IN("in", "${field} in (${variable})"),

        /** The not in. */
        NOT_IN("nin", "${field} not in (${variable})"),

        /** The orfer by asc. */
        ORFER_BY_ASC("ordra", "order by ${field} asc"),

        /** The orfer by desc. */
        ORFER_BY_DESC("ordrd", "order by ${field} desc");



        /** The code. */
        private final String code;

        /** The template. */
        private final String template;

        /**
         * Instantiates a new operators.
         *
         * @param code
         *            the code
         * @param template
         *            the template
         */
        Operators(String code, String template) {
            this.code = code;
            this.template = template;
        }

        /**
         * Code.
         *
         * @return the string
         */
        public final String code() {
            return code;
        }

        /**
         * Template.
         *
         * @return the string
         */
        public final String template() {
            return template;
        }
    }

    /**
     * Instantiates a new query condition model.
     */
    public QueryConditionModel() {

    }

    /**
     * Instantiates a new query condition model.
     *
     * @param key
     *            the key
     * @param field
     *            the field
     * @param variable
     *            the variable
     * @param operator
     *            the operator
     * @param condition
     *            the condition
     */
    public QueryConditionModel(String key, String field, String variable, String operator, String condition) {
        super();
        setKey(key);
        setField(field);
        setVariable(variable);
        setOperator(operator);
        setCondition(condition);
    }



    /**
     * Instantiates a new query condition model.
     *
     * @param map the map
     */
    public QueryConditionModel(LinkedHashMap<String, Object> map) {
        setKey((map.get("key") != null ) ? map.get("key").toString() : null);
        setField((map.get("field") != null ) ? map.get("field").toString() : null);
        setVariable((map.get("variable") != null ) ? map.get("variable").toString() : null);
        setOperator((map.get("operator") != null ) ? map.get("operator").toString() : null);
        setCondition((map.get("condition") != null ) ? map.get("condition").toString() : null);
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *            the new value
     */
    public void setValue(Object value) {
        this.value=value;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key
     *            the new key
     */
    public void setKey(String key) {
        this.key=key;
    }

    /**
     * Gets the field.
     *
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * Sets the field.
     *
     * @param field
     *            the new field
     */
    public void setField(String field) {
        this.field=field;
    }

    /**
     * Gets the variable.
     *
     * @return the variable
     */
    public String getVariable() {
        return variable;
    }

    /**
     * Sets the variable.
     *
     * @param variable
     *            the new variable
     */
    public void setVariable(String variable) {
        this.variable=variable;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(String type) {
        this.type=type;
    }

    /**
     * Gets the operator.
     *
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Sets the operator.
     *
     * @param operator
     *            the new operator
     */
    public void setOperator(String operator) {
        this.operator=operator;
    }

    /**
     * Gets the condition.
     *
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Sets the condition.
     *
     * @param condition
     *            the new condition
     */
    public void setCondition(String condition) {
        this.condition=condition;
    }

}
