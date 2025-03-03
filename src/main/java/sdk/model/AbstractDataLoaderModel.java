package sdk.model;
import jakarta.persistence.criteria.Join;

import java.util.List;
import java.util.Map;


public class AbstractDataLoaderModel {

    private Long start;
    private Long length;
    private Long size;
    private Boolean loadData;

    private List<?> data;

    private List<SearchDataModel> searchDataModel;

    private List<SortDataModel> sortDataModel;

    private List<JoinModel> join;

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Boolean getLoadData() {
        return loadData;
    }

    public void setLoadData(Boolean loadData) {
        this.loadData = loadData;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public List<SearchDataModel> getSearchDataModel() {
        return searchDataModel;
    }

    public void setSearchDataModel(List<SearchDataModel> searchDataModel) {
        this.searchDataModel = searchDataModel;
    }

    public List<SortDataModel> getSortDataModel() {
        return sortDataModel;
    }

    public void setSortDataModel(List<SortDataModel> sortDataModel) {
        this.sortDataModel = sortDataModel;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public List<JoinModel> getJoin() {
        return join;
    }

    public void setJoin(List<JoinModel> join) {
        this.join = join;
    }
}
