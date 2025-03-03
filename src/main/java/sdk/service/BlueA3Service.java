package sdk.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import sdk.model.ServiceExecutionResult;
import sdk.model.UserModel;

import java.util.Map;
@XRayEnabled
public interface BlueA3Service {


    public Map<String, Object> getTokenForUser(String realmName, String username, String password) throws Exception;


    String getTokenForAdminUser();


    ServiceExecutionResult logoutUser(String userName);


    UserModel getUserByUsername(String realm, String username) throws Exception;
}
