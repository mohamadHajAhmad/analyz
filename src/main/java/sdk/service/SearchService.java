package sdk.service;

import org.springframework.stereotype.Service;
import sdk.model.AbstractDataLoaderModel;
import sdk.model.ServiceExecutionResult;



public interface SearchService {


    ServiceExecutionResult search(Class<?> clazz, AbstractDataLoaderModel dataModel);
}
