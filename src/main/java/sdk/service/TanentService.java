package sdk.service;

import sdk.model.ServiceExecutionResult;

public interface TanentService {

    ServiceExecutionResult getPartner(String partnerCode);
}
