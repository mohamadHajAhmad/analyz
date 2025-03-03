/*************************************************************************
 * 
 * BluLogix CONFIDENTIAL
 * __________________
 * 
 *  [2017] BluLogix, LLC 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of BluLogix LLC and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to BluLogix LLC
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from BluLogix LLC.
 * Copyright (C) 2016 BluLogix, LLC 737 Walker Rd, Suite 3. Great Falls VA 22066
 * 
 * creation date: Mar 23, 2015
 * @author: Yasser El-ata
 */
package sdk.dao.implementation;


import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sdk.POJO.Accounts;
import sdk.StoredProcedure.AddAgentGroupSP;
import sdk.dao.SearchDAO;
import sdk.dao.util.DAOHelper;
import sdk.dao.util.Page;
import sdk.dao.util.SearchQueryContent;
import sdk.model.AbstractDataLoaderModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc

/**
 * The Class CISDAOImpl.
 */
@SuppressWarnings("all")
@Repository
public class SearchDAOImpl extends CISDAOImpl implements SearchDAO {



    @Override
    @Transactional(transactionManager="secondaryTransactionManager")
    public Long get() {
        Session currentSession = getSecondarySessionFactory().getCurrentSession();
        String sql = "SELECT count (account_number) FROM accounts";
        Query query = currentSession.createNativeQuery(sql,Long.class);
        Long count=0L;
        Object result = query.getSingleResult();
        System.out.println(result );
        if (result == null)
            return count;
        commit();
       count=Long.parseLong(result.toString());
        System.out.println(count );
        return count;
    }


    @Override
    @Transactional
    public Long get2() {
        Session currentSession = getSessionFactory().getCurrentSession();
        String sql = "SELECT count (account_number) FROM accounts";
        Query query = currentSession.createNativeQuery(sql,Long.class);
        Long count=0L;
        Object result = query.getSingleResult();
        System.out.println(result );
        if (result == null)
            return count;

        count=Long.parseLong(result.toString());
        System.out.println(count );
        return count;
    }

    @Override
    public Map SP() {
        Session currentSession = getSessionFactory().getCurrentSession();

        Map resultMap = new HashMap();

       //ActivateFullOrderSP activateFullOrderSP=new ActivateFullOrderSP(currentSession);
    AddAgentGroupSP addAgentGroupSP =new AddAgentGroupSP(currentSession);

       // StoredProcedureQuery query = currentSession.createStoredProcedureQuery("CUSTOMER_ORDER_PKG.ACTIVATE_FULL_ORDER");
//        query.registerStoredProcedureParameter("P_ORDER_ID", Long.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("P_START_DATE", Date.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("P_END_DATE", Date.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("P_ADDHOC_FLAG", String.class, ParameterMode.IN);
//
//        query.registerStoredProcedureParameter("P_COMMENT", String.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("P_SOURCE", String.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("P_USER", String.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("P_SURCHARGE_FLAG", String.class, ParameterMode.IN);
//
//        query.setParameter("P_ORDER_ID", 155);
//        query.setParameter("P_START_DATE", null);
//        query.setParameter("P_END_DATE", null);
//        query.setParameter("P_ADDHOC_FLAG", "N");
//        query.setParameter("P_COMMENT", "N");
//        query.setParameter("P_SOURCE", "MP");
//        query.setParameter("P_USER", "MP");
//        query.setParameter("P_SURCHARGE_FLAG", "N");
        try {
            //Map map=activateFullOrderSP.execute();
             resultMap=addAgentGroupSP.execute();
            System.out.println(resultMap.get("P_GROUP_ID"));
        }
        catch (Exception e) {
            //LOGGER.error("Cancle_Create_Subscription_Fail", e);
            //String message =e.getCause().getCause().getMessage();
            if (e != null && e.getCause().getCause().getMessage().toString().contains("ORA-20")) {
                resultMap.put("errorMessage",e.getCause().getCause().getMessage().toString().split("\\r?\\n")[0].split(":", 2)[1]);
            } else
                resultMap.put("errorMessage","erroee");
        }
        return resultMap;
    }

    @Override
    @Transactional(transactionManager="transactionManager")
    @Cacheable(value="dbcache",cacheManager = "springCM")
    public Accounts getAccountList() {
        Session currentSession = getSessionFactory().getCurrentSession();
//        String jpql = "SELECT accountNumber FROM Accounts c";
//        TypedQuery<Accounts> query = entityManager.createQuery(jpql, Accounts.class);

        //Accounts accounts=query.getSingleResult();
        Accounts accounts=currentSession.find(Accounts.class,"R124-0000002");

        accounts= (Accounts) find(Accounts.class,"R124-0000002");
//        List <String> s=new ArrayList<>();
//        s.add("R126-0000002");
//        List <Accounts> ss= (List<Accounts>) find(Accounts.class,s);
//        System.out.println(savePojo(accounts));
//        updatePojo(accounts);
//        CriteriaBuilder cb = currentSession.getCriteriaBuilder();
//        CriteriaQuery<Accounts> cq = cb.createQuery(Accounts.class);
//        Root<Accounts> book = cq.from(Accounts.class);
//        List<Predicate> predicates = new ArrayList<>();
//        predicates.add(cb.like(book.get("accountNumber"), "%" + "R124-0000002" + "%"));

        //cq.where(predicates.toArray(new Predicate[0]));
       // accounts =currentSession.createQuery(cq).getSingleResult();
        System.out.println(accounts.getAccountNumber());
    //System.out.println(ss.get(0).getAccountNumber());
        return accounts;


    }



    @Override
    public SearchQueryContent getList(Class<?> clazz, AbstractDataLoaderModel dataModel) {
        List data;
        SearchQueryContent searchQueryContent = new SearchQueryContent();
        Session currentSession = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder =currentSession.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(clazz);
        CriteriaQuery sizeCriteria = criteriaBuilder.createQuery(clazz);
        Root<?> root = criteriaQuery.from(clazz);

       List<Predicate> predicates = new ArrayList<>();

//        TypedQuery<BasicCustInfo> typedQuery = theEntityManager.createQuery(
//                query1.multiselect(table1.get("memberId"), table2.get("AcctId"))
//                        .where(conditions.toArray(new Predicate[] {}))
//        );

//
//        Subquery<?> sq2 = criteriaQuery.subquery(Accounts.class);
//        Root<?> subRoot2 = sq2.from(Accounts.class);
//        sq2.select(subRoot2.get("accountNumber")).where(criteriaBuilder.equal(subRoot2.get("accountNumber"),"R124-0000002" ));

//        //Subquery subquery = criteriaQuery.subquery(SspUser.class);

//        Subquery<SspUser> subquery = criteriaQuery.subquery(SspUser.class);
//        Root<SspUser> subRoot = subquery.from(SspUser.class);
//        subquery.select(subRoot.get("username")).where(criteriaBuilder.equal(subRoot.get("username"), sq2));

     //Predicate predicate=criteriaBuilder.equal(root.get("accountNumber"), subquery);

      //predicates.add(predicate);
        //Root<?> root2 = criteriaQuery.from(SspUser.class);


       //Join<Accounts,SspUser> item = root.join("sspUser");



      //Fetch<Accounts, SspUser> fetch = root.fetch("sspUser",JoinType.INNER);

//       Join<Accounts, SspUser> join = root.join("sspUser", JoinType.INNER);
//        //join.on(criteriaBuilder.like(root.get("accountNumber"), "%" + "R124-0000002" + "%"));
//        predicates.add(criteriaBuilder.like(join.get("accountNumber"), "%" + "R124-0000002" + "%"));
//        predicates.add(criteriaBuilder.like(join.get("firstName"), "%" + "Agility Communic_ations" + "%"));
//        predicates.add(criteriaBuilder.like(join.get("username"), join.get("firstName")));
//
//        join.on(predicates.toArray(new Predicate[0]));
////
////       // predicates.add(criteriaBuilder.equal(item.get("userId"),12432));
//       criteriaQuery.where(predicates.toArray(new Predicate[0]));

//dao



        searchQueryContent.setCriteria(criteriaQuery);
        searchQueryContent.setCriteriaCopy(sizeCriteria);
//
//
        Map<String, Field> fieldMap = DAOHelper.createFieldMap(clazz);
        searchQueryContent.setCriteriaBuilder(criteriaBuilder);
        searchQueryContent.setCriteria(criteriaQuery);
        searchQueryContent.setRoot(root);

        if (dataModel.getJoin()!=null) {
            Join<?, ?> join = DAOHelper.createJoin(searchQueryContent,fieldMap, dataModel.getJoin());

            if (join == null)
                return null;
            searchQueryContent.setJoin(join);
        }


       searchQueryContent = DAOHelper.createSearchDynamicQuery(searchQueryContent, dataModel.getSearchDataModel(), fieldMap,clazz);
        if (searchQueryContent == null)
            return null;


//     searchQueryContent = DAOHelper.createSortDynamicQuery(searchQueryContent, dataModel.getSortDataModel(), fieldMap,clazz);
//        if (searchQueryContent == null)
//            return null;

      criteriaQuery=searchQueryContent.getCriteria();

//        List<Order> orderList = new ArrayList();
//        orderList.add(criteriaBuilder.asc(root.get("accountNumber")));
//        criteriaQuery.orderBy(orderList);

        Page page = new Page(currentSession.createQuery(criteriaQuery), dataModel.getStart().intValue(), dataModel.getLength().intValue());
        if (page == null)
            return null;
        System.out.println(page.getList().size());
        System.out.println(page.getTotalResultsSize());
        commit();
        data = page.getList();
        searchQueryContent.setData(data);
        return searchQueryContent;
    }




}
