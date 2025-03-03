package sdk.service;


import org.springframework.cache.CacheManager;
import sdk.POJO.Accounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdk.dao.SearchDAO;
import sdk.model.AbstractDataLoaderModel;
import sdk.model.ServiceExecutionResult;

import java.util.Map;

@Service
public class AccountServiceImpl implements AccountsService {


    @Autowired
    CacheManager cacheManager;
    @Autowired
    private SearchDAO accountDAO;

    @Transactional
    @Override
    public ServiceExecutionResult get() {

        ServiceExecutionResult result =new ServiceExecutionResult();
        Map<String, Object> resultMap =accountDAO.SP();
//        Accounts count =accountDAO.getAccountList();
//        System.out.println(count);
        //System.out.println(cacheManager.getCache("dbcache").getNativeCache());
        result =checkProcedure(resultMap, result);
        if (!result.isExecutionSuccessful())
            return result;

        result.setSuccessCode("1");
        return result;
    }


    @Transactional
    @Override
    public ServiceExecutionResult get(AbstractDataLoaderModel dataModel) {
        ServiceExecutionResult result =new ServiceExecutionResult();
        accountDAO.getList(Accounts.class,dataModel);
//        Accounts count =accountDAO.getAccountList();
//        System.out.println(count);
        //System.out.println(cacheManager.getCache("dbcache").getNativeCache());
        result.setSuccessCode("1");
        return result;
    }

    public ServiceExecutionResult checkProcedure(Map<String, Object> resultMap, ServiceExecutionResult result) {
        if (resultMap == null)
            return resultWithErrorCode("5000", result);
        if (resultMap.containsKey("errorMessage")) {
            result.setExecutionSuccessful(false);
            result.setErrorCode("DB");
            result.setMessage(resultMap.get("errorMessage").toString());
            return result;
        }
        return result;
    }

    public ServiceExecutionResult resultWithErrorCode(String errorCode, ServiceExecutionResult result) {
        result.cleanResult();
        result.setExecutionSuccessful(false);
        result.setErrorCode(errorCode);
        return result;
    }

}
