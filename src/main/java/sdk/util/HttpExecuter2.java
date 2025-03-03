package sdk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sdk.model.ApiRequest;
import sdk.model.Pair;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class HttpExecuter2 {

    private static final Logger logger = LogManager.getLogger(HttpExecuter2.class);
    private  static HttpClient httpClient ;
    public static HttpClient getClientInstance() {
        if (httpClient == null) {
            httpClient = HttpClient.newBuilder()
                    // determines how long the client waits until a connection can be established.
                    .connectTimeout(Duration.ofSeconds(120))
                    .executor(Executors.newFixedThreadPool(5))
                    .build();
        }
        return httpClient;
    }

    public static Map<String, Object> execut(ApiRequest apiRequest) throws Exception {
        HttpResponse response = null;
        HttpRequest request = buildRequest(apiRequest);
        System.out.print(request.bodyPublisher().toString());
        HttpResponse.BodyHandler bodyHandler =getHttpResponseBodyHandler(apiRequest);
        response = getClientInstance().send(request,bodyHandler);
        return  handleResponse(response,apiRequest.getResponseBodyType());
    }


    public  static HttpResponse.BodyHandler getHttpResponseBodyHandler(ApiRequest apiRequest)   {
        String bodyType= apiRequest.getResponseBodyType().getType().getTypeName();
        return switch(bodyType) {
            case SystemConstants.Types.BYTE -> HttpResponse.BodyHandlers.ofByteArray();
            case SystemConstants.Types.INPUT_STREAM -> HttpResponse.BodyHandlers.ofInputStream();
            default -> HttpResponse.BodyHandlers.ofString();
        };

    }

    public static <T> T handleResponse(HttpResponse response, TypeReference typeReference) throws JsonProcessingException {
        //Get the status of the response
        int status = response.statusCode();
        // here we can handle other status code
        if (IsValidStatusCode(status)) {
            if (response == null || typeReference == null || response.body() ==null) {
                return null;
            }
            String returnType = typeReference.getType().getTypeName();
            // handle byte and inputstream and file already converted by handlers
            if (returnType.equals(SystemConstants.Types.BYTE)|| returnType.equals(SystemConstants.Types.INPUT_STREAM)||returnType.equals(SystemConstants.Types.FILE)) {
                return  (T) response.body();
            }
            return deserialize(response, typeReference);
        }
        return null;
    }

    public static <T> T deserialize(HttpResponse response, TypeReference typeReference) throws JsonProcessingException {
        String returnType = typeReference.getType().getTypeName();
        // handling string as json or pure string
        String respBody = response.body().toString();
        Optional contentType = response.headers().firstValue("Content-Type");
        // ensuring a default content type
        String contentTypeStr= contentType == null? "application/json": contentType.get().toString();
        if (jsonUtils.isJsonMime(contentTypeStr)) {
            return jsonUtils.jsonToObjectWithDateFormatter(respBody, typeReference);
        }else if (returnType.equals("java.lang.String")) {
            // Expecting string, return the raw response body.
            return (T) respBody;
        }
        return null;
    }


    public static HttpRequest buildRequest(ApiRequest request) throws JsonProcessingException, FileNotFoundException {
        String uri = buildUrl(request.getUrlString(),request.getParams());
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(uri));
        // build request body
        HttpRequest.BodyPublisher body =createRequestBody(request);
        requestBuilder.method(request.getMethod(),  body);
        // add headers
        if (request.getHeaders() != null) {
            addHeaders(requestBuilder,request.getHeaders());
        }

        if (request.getAuthorizationType() != null && request.getAuthorizationType().equals(SystemConstants.Authorization.BASIC)) {
            setBasicAuthorizationHeader(requestBuilder, request.getUsername(), request.getPassword());
        } else if (request.getAuthorizationType() != null && request.getAuthorizationType().equals(SystemConstants.Authorization.BEARER)) {
            setAuthorizationHeader(request,requestBuilder);
        }

        return  requestBuilder.build();

    }

    public static String buildUrl(String path, List<Pair> queryParams) throws JsonProcessingException {
        final StringBuilder url = new StringBuilder();
        url.append(path);
        if (queryParams != null && !queryParams.isEmpty() ) {
            // support (constant) query string in `path`, e.g. "/posts?draft=1"
            String prefix = path.contains("?") ? "&" : "?";
            for (Pair param : queryParams) {
                if (param.getValue() != null) {
                    if (prefix != null) {
                        url.append(prefix);
                        prefix = null;
                    } else {
                        url.append("&");
                    }
                    String value = parameterToString(param.getValue());
                    url.append(escapeString(param.getName())).append("=").append(escapeString(value));
                }
            }
        }
        return url.toString();
    }

    static String escapeString(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }
    static String parameterToString(Object param) throws JsonProcessingException {
        if (param == null) {
            return "";
        } else if (param instanceof Date || param instanceof OffsetDateTime || param instanceof LocalDate) {
            //Serialize to json string and remove the " enclosing characters
            String jsonStr = JSONHelper.objectToJson(param);
            return jsonStr.substring(1, jsonStr.length() - 1);
        } else if (param instanceof Collection) {
            StringBuilder b = new StringBuilder();
            for (Object o : (Collection)param) {
                if (b.length() > 0) {
                    b.append(",");
                }
                b.append(o);
            }
            return b.toString();
        } else {
            return String.valueOf(param);
        }
    }


    public static HttpRequest.BodyPublisher createRequestBody(ApiRequest request) throws FileNotFoundException, JsonProcessingException {
        // build request body for post and put request
        if (request.getBody() != null && (request.getMethod().equals(SystemConstants.HttpMethods.POST) || request.getMethod().equals(SystemConstants.HttpMethods.PUT))) {
            if(request.getBodyType().equals(SystemConstants.HttpBodyTypes.STRING) ){
                String body = request.getBody().toString();
                return  HttpRequest.BodyPublishers.ofString(body);
            }
            // not tested
            else if(request.getBodyType().equals(SystemConstants.HttpBodyTypes.FILE) ){
                String path="";
                try {
                    return  HttpRequest.BodyPublishers.ofFile(Path.of(path));
                } catch (FileNotFoundException e) {
                    throw e;
                }
            }
            // not tested
            else if(request.getBodyType().equals(SystemConstants.HttpBodyTypes.BYTES)){
                return HttpRequest.BodyPublishers.ofByteArray((byte[]) request.getBody());
            }
            // not tested
            else if(request.getBodyType().equals(SystemConstants.HttpBodyTypes.INPUT_STREAM)){
                return  HttpRequest.BodyPublishers.ofInputStream((Supplier<? extends InputStream>) request.getBody());
            }
        }
        return   HttpRequest.BodyPublishers.noBody();

    }

    public static void addHeaders(HttpRequest.Builder requestBuilder, List<Pair> headers) {
        for (Pair header : headers) {
            requestBuilder.setHeader(header.getName(), header.getStringValue());
        }
    }

    public static void setBasicAuthorizationHeader(HttpRequest.Builder requestBuilder, String userName, String password) {
        requestBuilder.setHeader("Authorization", "Basic " + StringUtils.encodeString(userName + ":" + password));

    }
    public static void setAuthorizationHeader(ApiRequest request,HttpRequest.Builder requestBuilder)  {
        requestBuilder.setHeader("Authorization", "Bearer " + request.getToken());
    }
    public  static boolean IsValidStatusCode(int statusCode){
        return  statusCode >= 200 && statusCode < 300;
    }

}
