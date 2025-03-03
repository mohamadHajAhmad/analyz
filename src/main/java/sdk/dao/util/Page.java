package sdk.dao.util;

import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.query.Query;
import sdk.dao.CISDAO;
import sdk.dao.implementation.CISDAOImpl;

import java.util.List;

@SuppressWarnings("all")
public class Page {


    /** The results. */
    private List results;

    /** The page size. */
    private int pageSize;

    /** The page. */
    private int page;

    /** The total results size. */
    private Long totalResultsSize;


    /**
     * Instantiates a new page.
     *
     * @param criteria            the criteria to be executed.
     * @param start            the start, starting cursor point.
     * @param length the length
     */
    public Page(Query query, int start, int length) {
        this.page = start;
        this.pageSize = length;
        totalResultsSize = query.setFirstResult(0).stream().count();
        System.out.println(totalResultsSize);
        results = query.setMaxResults(length).setFirstResult(start).getResultList();

    }

    /**
     * Checks if has next page.
     *
     * @return true, if has next page
     */
    public boolean hasNextPage() {
        return results.size() > pageSize;
    }

    /**
     * Checks if has previous page.
     *
     * @return true, if has previous page
     */
    public boolean hasPreviousPage() {
        return page > 0;
    }


    /**
     * Gets the list.
     *
     * @return the list
     */
    public List getList() {
        return hasNextPage() ? results.subList(0, pageSize - 1) : results;
    }


    /**
     * Sets the total results size.
     *
     * @param totalResultsSize
     *            the new total results size
     */
    public void setTotalResultsSize(Long totalResultsSize) {
        this.totalResultsSize = totalResultsSize;
    }

    /**
     * Gets the total results size.
     *
     * @return the total results size
     */
    public Long getTotalResultsSize() {
        return totalResultsSize;
    }


    }
