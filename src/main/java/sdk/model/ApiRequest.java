package sdk.model;


import com.fasterxml.jackson.core.type.TypeReference;
import sdk.util.SystemConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ApiRequest {
    private String urlString;
    private String username;
    private String password;
    private Integer port;
    private String method = SystemConstants.HttpMethods.POST;
    private  String bodyType= SystemConstants.HttpBodyTypes.STRING;
    private List<Pair> headers;
    private List<Pair> params;
    private Object body;
    private String authorizationType = SystemConstants.Authorization.BEARER;
    private String token;
    private TypeReference responseBodyType;


    public ApiRequest(){
        responseBodyType= new TypeReference<HashMap>() {};

    }
    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Pair> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Pair> headers) {
        this.headers = headers;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        if(body==null)body= new HashMap<>();

        this.body = body;
    }


    public void setBody(Object k1,Object v1) {
        HashMap body= new HashMap();
        body.put(k1,v1);
        this.body = body;
    }
    public void setBody(Object k1,Object v1,Object k2,Object v2) {
        HashMap body= new HashMap();
        body.put(k1,v1);
        body.put(k2,v2);
        this.body = body;
    }
    public void setBody(Object k1,Object v1,Object k2,Object v2,Object k3,Object v3) {
        HashMap body= new HashMap();
        body.put(k1,v1);
        body.put(k2,v2);
        body.put(k3,v3);
        this.body = body;
    }
    public void setBody(Object k1,Object v1,Object k2,Object v2,Object k3,Object v3,Object k4,Object v4) {
        HashMap body= new HashMap();
        body.put(k1,v1);
        body.put(k2,v2);
        body.put(k3,v3);
        body.put(k4,v4);
        this.body = body;
    }


    public String getAuthorizationType() {
        return authorizationType;
    }

    public void setAuthorizationType(String authorizationType) {
        this.authorizationType = authorizationType;
    }



    public List<Pair> getParams() {
        return params;
    }
    public void setParam(Pair param) {
        this.params = Arrays.asList(param);
    }

    public void setParams(List<Pair> params) {
        this.params = params;
    }
    public void setParams(String k1,Object v1,String k2,Object v2) {
        addParam(new Pair(k1,v1));
        addParam(new Pair(k2,v2));

    }
    public void setParams(String k1,Object v1,String k2,Object v2,String k3,Object v3) {
        addParam(new Pair(k1,v1));
        addParam(new Pair(k2,v2));
        addParam(new Pair(k3,v3));
    }
    public void setParams(String k1,Object v1,String k2,Object v2,String k3,Object v3,String k4,Object v4) {
        addParam(new Pair(k1,v1));
        addParam(new Pair(k2,v2));
        addParam(new Pair(k3,v3));
        addParam(new Pair(k4,v4));
    }
    public void setParam(String k1, Object v1) {
        addParam(new Pair(k1,v1));
    }
    public void addParam(Pair param) {
        if (this.params==null)
            params= new ArrayList<>();
        params.add(param);
    }
    public void addParam(String k1,Object v1) {
        addParam(new Pair(k1,v1));
    }
    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TypeReference getResponseBodyType() {
        return responseBodyType;
    }

    public void setResponseBodyType(TypeReference responseBodyType) {
        this.responseBodyType = responseBodyType;
    }
}
