package sdk.model;

import java.util.List;

public class JoinDataModel extends AbstractModel{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String searchField;
    private String secondSearchField;
    private Object searchValues;
    private String operator;
    private String condition;



    public void setValue(Object value) {
        searchValues = value;
    }

    public Object getValue() {
        return searchValues;
    }

    public void setValues(List<Object> values) {
        searchValues = values;
    }

    public List<Object> getValues() {
        return (List<Object>) searchValues;
    }


    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSecondSearchField() {
        return secondSearchField;
    }

    public void setSecondSearchField(String secondSearchField) {
        this.secondSearchField = secondSearchField;
    }

    public Object getSearchValues() {
        return searchValues;
    }

    public void setSearchValues(Object searchValues) {
        this.searchValues = searchValues;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
