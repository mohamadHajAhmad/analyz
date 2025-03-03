package sdk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdk.dao.TanentDao;
import sdk.model.ServiceExecutionResult;
@Service
public class TanentServiceImpl implements TanentService{

    @Autowired
    private TanentDao tanentDao;

    @Transactional
    @Override
    public ServiceExecutionResult getPartner(String partnerCode) {
        ServiceExecutionResult result = new ServiceExecutionResult();
        try {
            partnerCode = tanentDao.getPartner(partnerCode);
            if (partnerCode == null) {
                result.setExecutionSuccessful(false);
                result.setErrorCode("1033");
                return result;
            }
            result.setReturnValue(partnerCode);
            return result;

        } catch (Exception e) {
            result.setExecutionSuccessful(false);
            result.setErrorCode("1033");
            return result;
        }
    }
}
