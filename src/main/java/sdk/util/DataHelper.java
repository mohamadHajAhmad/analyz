package sdk.util;

public class DataHelper {

    /**
     * Gets the base url from domain.
     *
     * @param domain the domain
     * @return the base url from domain
     */
    public static String getBaseUrlFromDomain(String domain) {
        String baseUrl = null;
        if(domain.startsWith("http")){
            baseUrl = domain.substring(domain.indexOf("//")+2, domain.length());
        }else{
            baseUrl = domain;
        }
        if(baseUrl.contains("/")){
            baseUrl = baseUrl.substring(0, baseUrl.indexOf("/"));
        }
        if(baseUrl.endsWith("/")){
            baseUrl = baseUrl.substring(0,baseUrl.length()-1);

        }
        if(baseUrl == null){
            throw new IllegalStateException("Not able to resolve baseURL from the request path!");
        }
        return baseUrl;
    }
}
