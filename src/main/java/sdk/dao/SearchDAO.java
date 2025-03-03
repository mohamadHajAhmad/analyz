
package sdk.dao;




import sdk.POJO.Accounts;
import sdk.model.AbstractDataLoaderModel;
import sdk.dao.util.SearchQueryContent;

import java.util.Map;


public interface SearchDAO extends CISDAO{

    Long get();
    Long get2();

    Map SP();

    Accounts getAccountList();


    public SearchQueryContent getList(Class<?> clazz, AbstractDataLoaderModel dataModel);
}
