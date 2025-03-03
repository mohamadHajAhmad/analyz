package sdk.service;

import sdk.model.AbstractDataLoaderModel;
import sdk.model.ServiceExecutionResult;

public interface AccountsService {

    ServiceExecutionResult get();

    ServiceExecutionResult get(AbstractDataLoaderModel dataModel);
}
