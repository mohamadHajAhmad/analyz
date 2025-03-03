package sdk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdk.dao.SearchDAO;
import sdk.dao.util.DAOHelper;
import sdk.dao.util.SearchQueryContent;
import sdk.model.*;
import sdk.util.restBusinessDataValidation.DataValidation;

import java.util.ArrayList;
import java.util.List;


@Service
public class SearchServiceImpl implements  SearchService{



    @Autowired
    private SearchDAO accountDAO;


    @Transactional
    @Override
    public ServiceExecutionResult search(Class<?> clazz, AbstractDataLoaderModel dataModel) {

        ServiceExecutionResult result = new ServiceExecutionResult();
        if (dataModel.getStart() == null)
            dataModel.setStart(0L);
        if (dataModel.getLength() == null)
            dataModel.setLength(10L);

        /**-1 to get all data**/
        if (dataModel.getLength() != -1) {
            result = DataValidation.checkPaginationLengths(dataModel.getStart(), dataModel.getLength(), result);
            if (!result.isExecutionSuccessful())
                return result;
        }

//       List<SearchDataModel> newSearchDataModel=dataModel.getSearchDataModel();
//       SearchDataModel searchDataModel1 = new SearchDataModel();
//        searchDataModel1.setSearchField("accountNumber");
//        searchDataModel1.setSubSearchField("accountNumber");
//        searchDataModel1.setClazz(SspUser.class);
//        List<SubSearchDataModel> ss=new ArrayList<>();
//
//        SubSearchDataModel subSearchDataModel=new SubSearchDataModel();
//        subSearchDataModel.setSubQuer(true);
//        subSearchDataModel.setOperator("eq");
//        subSearchDataModel.setSearchValues("R124-0000002");
//        subSearchDataModel.setCondition("and");
//        subSearchDataModel.setSearchField("username");
//
///** create sub 2**/
//        SearchDataModel searchDataModel12 = new SearchDataModel();
//        searchDataModel12.setSearchField("accountNumber");
//        searchDataModel12.setSubSearchField("accountNumber");
//        searchDataModel12.setClazz(Accounts.class);
//        List<SubSearchDataModel> ss2=new ArrayList<>();
//
//        SubSearchDataModel subSearchDataModel1=new SubSearchDataModel();
//        subSearchDataModel1.setSubQuer(true);
//        subSearchDataModel1.setOperator("eq");
//        subSearchDataModel1.setSearchValues("R124-0000002");
//        subSearchDataModel1.setCondition("and");
//        subSearchDataModel1.setSearchField("accountNumber");
//        ss2.add(subSearchDataModel1);
//        searchDataModel12.setSubSearchDataModelList(ss2);
//        subSearchDataModel.setSearchDataModel(searchDataModel12);
//  /* end sub 2*/
//        ss.add(subSearchDataModel);
//        searchDataModel1.setSubSearchDataModelList(ss);
//        newSearchDataModel.add(searchDataModel1);
//
//       dataModel.setSearchDataModel(newSearchDataModel);


        List<JoinModel> join =new ArrayList<>();
        JoinModel join2 =new JoinModel("sspUser","INNER");
        List<JoinDataModel>joinModelList=new ArrayList<>();
        JoinDataModel joinDataModel=new JoinDataModel();
        joinDataModel.setSearchField("accountNumber");
        joinDataModel.setSecondSearchField("sspUser.accountNumber");
        joinDataModel.setCondition("and");
        joinModelList.add(joinDataModel);
        join2.setJoinDataModel(joinModelList);
        join.add(join2);
        dataModel.setJoin(join);
        SearchQueryContent searchQueryContent = accountDAO.getList(clazz, dataModel);

        if (searchQueryContent == null)
            return resultWithErrorCode("1000", result);

        List dataList = searchQueryContent.getData();
        List<AbstractModel> data = new ArrayList<>();
        if (!dataList.isEmpty() && dataList.get(0) == null) {
            dataModel.setData(data);
            result.setReturnValue(dataModel);
            return result;
        }
        for (Object object : dataList) {
            data.add(DAOHelper.createModelDataFromObject(object));
        }
        dataModel.setData(data);
        result.setReturnValue(dataModel);
        return result;
    }


    /**
     * Result with error code.
     *
     * @param errorCode the error code
     * @param result    the result
     * @return the service execution result
     */
    public ServiceExecutionResult resultWithErrorCode(String errorCode, ServiceExecutionResult result) {
        result.cleanResult();
        result.setExecutionSuccessful(false);
        result.setErrorCode(errorCode);
        return result;
    }
}
