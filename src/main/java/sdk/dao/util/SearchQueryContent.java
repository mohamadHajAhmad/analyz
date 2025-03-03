package sdk.dao.util;
import java.util.List;
import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

public class SearchQueryContent

{

    private CriteriaQuery criteria;
    private CriteriaQuery criteriaCopy;

    private List<Object> values;

    CriteriaBuilder criteriaBuilder;

    private List data;
    private Long count;

    private Root<?> root;

    private Join<?,?>  join;



    public Join<?, ?> getJoin() {
        return join;
    }

    public void setJoin(Join<?, ?> join) {
        this.join = join;
    }



    public SearchQueryContent() {}

    public CriteriaBuilder getCriteriaBuilder() {
        return criteriaBuilder;
    }

    public void setCriteriaBuilder(CriteriaBuilder criteriaBuilder) {
        this.criteriaBuilder = criteriaBuilder;
    }

    public CriteriaQuery getCriteria() {
        return criteria;
    }

    public void setCriteria(CriteriaQuery criteria) {
        this.criteria = criteria;
    }

    public CriteriaQuery getCriteriaCopy() {
        return criteriaCopy;
    }

    public void setCriteriaCopy(CriteriaQuery criteriaCopy) {
        this.criteriaCopy = criteriaCopy;
    }


    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public Root<?> getRoot() {
        return root;
    }

    public void setRoot(Root<?> root) {
        this.root = root;
    }
}
