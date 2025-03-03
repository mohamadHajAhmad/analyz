package sdk.model;

import java.util.Map;

public class SortDataModel extends AbstractModel{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String sortField;
    private String direction;

    public SortDataModel() {}

    public SortDataModel(String sortField, String direction) {
        super();
        this.sortField = sortField;
        this.direction = direction;
        set("sortField", sortField);
        set("direction", direction);
    }

    public SortDataModel(Map<String, Object> properties) {
        setProperties(properties);
    }

    public void buildObject() {
        setSortField(get("sortField"));
        setDirection(get("direction"));
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
