package sdk.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.common.VerificationException;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.jose.jws.JWSInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sdk.model.*;
import sdk.util.HttpExecuter;
import sdk.util.HttpExecuter2;
import sdk.util.JSONHelper;
import sdk.util.SystemConstants;
import sdk.util.restBusinessDataValidation.DataValidation;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@XRayEnabled
public class BlueA3ServiceImpl implements BlueA3Service{
    /** The token admin. */
    private String tokenAdmin = null;

   @Value("${keycloak.url}")
    private String keycloakUrl;

    @Value("${blueA3.realm}")
    private String apiRealmName;

   @Value("${blueA3.rest.realmsUrl}")
    private String blueA3RestRealmsUrl;


   @Value("${blueA3.rest.port.enable}")
    private Boolean blueA3RestPortEnable;

   @Value("${blueA3.rest.port}")
    private Integer blueA3RestPort;


    @Value("${blueA3.rest.ssl}")
    private Boolean blueA3RestSsl;

    /** The blue a3 rest username. */
   @Value("${blueA3.rest.username}")
    private String blueA3RestUsername;

    /** The blue a3 rest password. */
    @Value("${blueA3.rest.password}")
    private String blueA3RestPassword;

  @Value("${blueA3.rest.adminUrl}")
    private String blueA3RestAdminUrl;


    @Value("${keycloak.realm}")
    private String realmName;

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getTokenForUser(String realmName, String username, String password) throws Exception {

        String clientId = "MARKET-PLACE";
        String clientSecret = "fd01f0a5-e47b-4193-8744-649998e5646f";
        UserModel model = getTokenForUser(realmName, username, password, clientId, clientSecret);
        if (model == null)
            return null;
        if (model.get("firstLogin") != null) {
            Object firstLoginList = model.get("firstLogin");
            if (firstLoginList != null && firstLoginList.toString().length() > 0) {
                model.set("firstLogin", firstLoginList.toString());
            }
        }
        Map<String, Object> attributeMap = model.getAttributes();
        if (attributeMap != null) {
            Object agentUser = attributeMap.get("agentUser");
            model.set("agentUser", agentUser == null ? "false" : agentUser);
        } else
            model.set("agentUser", "false");
        return model;
    }


    public UserModel getTokenForUser(String realm, String username, String password, String clientId, String clientSecret) throws Exception {
        ServiceExecutionResult result = new ServiceExecutionResult();

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        String body = "username=" + username + "&password=" + URLEncoder.encode(password, "UTF-8") + "&grant_type=password";

        String urlString = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        Map<String, Object> response = request(urlString, clientId, clientSecret, HttpExecuter.POST, headers, body, SystemConstants.Authorization.BASIC);
        Object responseBody = response.get("body");
        if (DataValidation.isEmptyOrNull(responseBody))
            return null;

        Map<String, Object> response2 = request2(urlString, clientId, clientSecret, HttpExecuter.POST, headers, body, SystemConstants.Authorization.BASIC);


        Map<String, Object> resultMap = new ObjectMapper().readValue(responseBody + "", HashMap.class);

        if (resultMap.get("access_token") == null && resultMap.get("error_description") != null) {
            if ("Client secret not provided in request".equals(resultMap.get("error_description"))) {
                return null;
            } else if ("Client not allowed for direct access grants".equals(resultMap.get("error_description"))) {
                result = addDirectAccesGrant(realm, clientId);
                if (!result.isExecutionSuccessful()) {
                    return null;
                }
                response = request(urlString, clientId, clientSecret, HttpExecuter.POST, null, body, SystemConstants.Authorization.BASIC);
                responseBody = response.get("body");
                if (DataValidation.isEmptyOrNull(responseBody))
                    return null;

                resultMap = new ObjectMapper().readValue(responseBody + "", HashMap.class);
                if (resultMap.get("access_token") == null)
                    return null;

            } else
                return null;
        }

        JWSInput input;
        try {
            input = new JWSInput(resultMap.get("access_token").toString());
        } catch (JWSInputException e) {
            throw new VerificationException("Couldn't parse token", e);
        }

        Map<String, Object> token;
        try {
            token = input.readJsonContent(HashMap.class);
        } catch (JWSInputException e) {
            throw new VerificationException("Couldn't parse token signature", e);
        }
        UserModel model = new UserModel(token);
        model.put("access_token", resultMap.get("access_token"));
        return model;
    }


    private Map<String, Object> request(String url, String username, String password, String httpMethod, Map<String, String> headers, String jsonBody, String authorizationMethod) throws Exception {
        Map<String, Object> response;

        Integer port = blueA3RestPortEnable ? blueA3RestPort : null;
        if (headers == null) {
            headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
        }
        if (SystemConstants.Authorization.BEARER.equals(authorizationMethod) && DataValidation.isEmptyOrNull(tokenAdmin))
            tokenAdmin = getTokenForAdminUser();
        try {
            response = HttpExecuter.execut(url, username, password, port, blueA3RestSsl, httpMethod, headers, jsonBody, authorizationMethod, tokenAdmin, true);
            if (isAdminTokenExpired(response)) {
                tokenAdmin = getTokenForAdminUser();
                response = HttpExecuter.execut(url, username, password, port, blueA3RestSsl, httpMethod, headers, jsonBody, authorizationMethod, tokenAdmin, true);
            }
        } catch (Exception e) {
           throw e;
        }
        return response;
    }

    private Map<String, Object> request2(String url, String username, String password, String httpMethod, Map<String, String> headers, String jsonBody, String authorizationMethod) throws Exception {
        Map<String, Object> response;

        Integer port = blueA3RestPortEnable ? blueA3RestPort : null;
        if (headers == null) {
            headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
        }
        if (SystemConstants.Authorization.BEARER.equals(authorizationMethod) && DataValidation.isEmptyOrNull(tokenAdmin))
            tokenAdmin = getTokenForAdminUser();
        try {
            ApiRequest apiRequest=new ApiRequest();
            apiRequest.setUrlString(url);
            apiRequest.setPort(port);

            Pair pair=new Pair("Content-Type", "application/x-www-form-urlencoded");
            List<Pair> list =new ArrayList<>();
            list.add(pair);
            apiRequest.setHeaders(list);
            apiRequest.setMethod(SystemConstants.HttpMethods.POST);
            apiRequest.setBody(jsonBody);
            apiRequest.setAuthorizationType(SystemConstants.Authorization.BASIC);
            apiRequest.setPassword(password);
            apiRequest.setUsername(username);
            response = HttpExecuter2.execut(apiRequest);
            if (isAdminTokenExpired(response)) {
                tokenAdmin = getTokenForAdminUser();
                response = HttpExecuter2.execut(apiRequest);
            }
        } catch (Exception e) {
            throw e;
        }
        return response;
    }

    public boolean isAdminTokenExpired(Map<String, Object> response) {
        return response == null || (response.get("status") != null && response.get("status").toString().toLowerCase().equals("http/1.1 401 unauthorized")) || (response.get("body") != null && response.get("body").toString().trim().toLowerCase().equals("bearer"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getTokenForAdminUser() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        String body = "username=" + blueA3RestUsername + "&password=" + blueA3RestPassword + "&grant_type=password&client_id=admin-cli";
        String url = keycloakUrl + blueA3RestRealmsUrl;
        try {
            Map<String, Object> response = request(url, null, null, HttpExecuter.POST, headers, body, null);
            Object responseBody = response.get("body");
            if (!DataValidation.isEmptyOrNull(responseBody)) {
                Map<String, Object> resultMap = JSONHelper.toMap(responseBody + "");
                return resultMap.get("access_token") + "";
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            return "";
        }
        return "";
    }

    private ServiceExecutionResult addDirectAccesGrant(String realm, String clientId){
        ServiceExecutionResult result = new ServiceExecutionResult();

        result = getClients(realm);
        if(!result.isExecutionSuccessful())
            return result;

        List<Map<String,Object>> clients = (List<Map<String,Object>>) result.getReturnValue();

        for(Map<String, Object> client : clients)
            if(client.get("clientId").toString().equals(clientId)) {
                ClientModel clientModel = new ClientModel();
                clientModel.putAll(client);
                clientModel.put("directAccessGrantsEnabled", true);
                return updateClient(realm, clientModel);
            }
        result.setErrorCode("1000");
        return result;
    }


    public ServiceExecutionResult getClients(String realm) {
        ServiceExecutionResult result = new ServiceExecutionResult();
        Map<String, Object> response = null;
        // Validation
        Map<String, Object> validationMap =  new HashMap<>();
        validationMap.put("realm", realm);
        result = DataValidation.checkRequiredValues(validationMap);
        if(!result.isExecutionSuccessful()){
            return result;
        }

        String url = keycloakUrl + blueA3RestAdminUrl + "/" + realm + "/clients";
        try {
            response = request(url, null, null, HttpExecuter.GET, null, "", SystemConstants.Authorization.BEARER);
        } catch (Exception e) {
            return DataValidation.resultWithErrorCode("5000", result);
        }

        // response handle
        if (!(response == null) && response.get("status") != null) {
            if (response.get("status").toString().contains("200")) {
                Object body = response.get("body");
                if (body != null && !body.toString().isEmpty()) {
                    List<Map<String,Object>> clients = JSONHelper.toListGeneric(body.toString());
                    result.setReturnValue(clients);
                    result.setExecutionSuccessful(true);
                    return result;
                }
            }
        }
        result.setExecutionSuccessful(false);
        result.setErrorCode("20000");
        result.setMessage("Failed to get clients");
        return result;

    }

    private ServiceExecutionResult updateClient(String realm, ClientModel clientModel){
        ServiceExecutionResult result = new ServiceExecutionResult();
        Map<String, Object> response;

        String url = keycloakUrl + blueA3RestAdminUrl+ "/" + realm + "/clients/" + clientModel.getId();

        String jsonBody = JSONHelper.toJsonFromModel(clientModel);
        try {
            response = request(url, null, null, HttpExecuter.PUT, null, jsonBody, SystemConstants.Authorization.BEARER);
        } catch (Exception e) {
            return DataValidation.resultWithErrorCode("5000", result);
        }
        if (response != null && response.get("status") != null)
            if (response.get("status").toString().contains("204")) { //No content
                result.setExecutionSuccessful(true);
                return result;
            }

        result.setExecutionSuccessful(false);
        result.setErrorCode("20000");
        result.setMessage("Failed to update client");
        return result;
    }


    @Override
    public ServiceExecutionResult logoutUser(String userName) {

        ServiceExecutionResult result = new ServiceExecutionResult();
        String realm =realmName;


        if (DataValidation.isEmptyOrNull(userName) || DataValidation.isEmptyOrNull(realm)) {
            result.setExecutionSuccessful(false);
            result.setErrorCode("1000");
            return result;
        }
        try {
            UserModel userModel = getUserByUsername(realm, userName);
            String id = userModel.getId();
            if (DataValidation.isEmptyOrNull(id)) {
                System.out.println("1957 " +  id);
                result.setExecutionSuccessful(false);
                result.setErrorCode("1000");
                return result;
            }


            String url = keycloakUrl + blueA3RestAdminUrl + "/" + realm + "/users/" + id + "/logout";
            Map<String, Object> response = request(url, null, null, HttpExecuter.POST, null, "", SystemConstants.Authorization.BEARER);
            String responseBody = (String) response.get("body");
            if (!DataValidation.isEmptyOrNull(responseBody)) {
                result.setExecutionSuccessful(true);
                result.setSuccessCode("6004");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setExecutionSuccessful(false);
            result.setErrorCode("1000");
            return result;
        }
        result.setExecutionSuccessful(true);
        result.setSuccessCode("6004");
        return result;
    }


    @Override
    public UserModel getUserByUsername(String realm, String username) throws Exception {
        String url = keycloakUrl + blueA3RestAdminUrl + "/" + realm + "/users" + "?username=" + username;

        String responseBody = "";
        try {
            Map<String, Object> response = request(url, null, null, HttpExecuter.GET, null, "", SystemConstants.Authorization.BEARER);
            responseBody = (String)response.get("body");
        } catch (Exception e) {
            throw e;
        }

        if (!DataValidation.isEmptyOrNull(responseBody)) {
            List<Map<String, Object>> temp = new ObjectMapper().readValue(responseBody, List.class);
            for (Map<String, Object> map : temp) {
                if (map.get("username").toString().equalsIgnoreCase(username)) {
                    UserModel model = new UserModel();
                    model.setProperties(map);
                    return model;
                }
            }
        }
        return null;
    }

}
