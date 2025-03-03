package sdk.model;

import java.util.List;
import java.util.Map;

public class SearchDataModel extends AbstractModel{



    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public SearchDataModel(){

    }

    private String searchField;
    private Object searchValues;
    private String operator;
    private String condition;

    private Class<?> clazz;

    private Boolean subQuer;

    private String subSearchField;

    private List<SubSearchDataModel>  subSearchDataModelList;


    public SearchDataModel(String searchField, Object searchValues, String operator, String condition, Class<?> clazz, Boolean subQuer, String subSearchField) {
        super();
        this.searchField = searchField;
        this.searchValues = searchValues;
        this.operator = operator;
        this.condition = condition;
        this.clazz = clazz;
        this.subQuer = subQuer;
        this.subSearchField = subSearchField;
    }

    public SearchDataModel(String searchField, String operator) {
        super();
        this.setSearchField(searchField);
        this.setOperator(operator);
        set("searchField", searchField);
        set("operator", operator);
    }

    public SearchDataModel(String searchField,  Object searchValues, String operator) {
        super();
        this.setSearchField(searchField);
        this.setSearchValues(searchValues);
        this.setOperator(operator);
        set("searchField", searchField);
        set("searchValues", searchValues);
        set("operator", operator);
    }


    public SearchDataModel(String searchField,  Object searchValues, String operator, String condition) {
        super();
        this.setSearchField(searchField);
        this.setSearchValues(searchValues);
        this.setOperator(operator);
        this.setCondition(condition);
        set("searchField", searchField);
        set("searchValues", searchValues);
        set("operator", operator);
        set("condition", condition);
    }

    public SearchDataModel(Map<String, Object> properties) {
        setProperties(properties);
    }

    public String getSearchField() {
        return searchField;
    }
    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }

    @SuppressWarnings("unchecked")
    public void buildObject(){
        this.setSearchField(get("searchField"));
        if(get("searchValues") instanceof List){
            this.setSearchValues((List<Object>) get("searchValues"));
        }else{
            this.setSearchValues(get("searchValues"));
        }
        this.setOperator(get("operator"));
        this.setCondition(get("condition"));
        this.setSubSearchField(get("subSearchField"));
        this.setSubQuer(get("subQuer"));
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Object getSearchValues() {
        return searchValues;
    }

    public void setSearchValues(Object searchValues) {
        this.searchValues = searchValues;
    }

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



    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Boolean getSubQuer() {
        return subQuer;
    }

    public void setSubQuer(Boolean subQuer) {
        this.subQuer = subQuer;
    }

    public String getSubSearchField() {
        return subSearchField;
    }

    public void setSubSearchField(String subSearchField) {
        this.subSearchField = subSearchField;
    }

    public List<SubSearchDataModel> getSubSearchDataModelList() {
        return subSearchDataModelList;
    }

    public void setSubSearchDataModelList(List<SubSearchDataModel> subSearchDataModelList) {
        this.subSearchDataModelList = subSearchDataModelList;
    }
}
