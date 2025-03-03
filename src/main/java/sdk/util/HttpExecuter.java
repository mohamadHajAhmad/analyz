package sdk.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;


public class HttpExecuter {

    /** The Constant GET. */
    public static final String GET = "get";

    /** The Constant POST. */
    public static final String POST = "post";

    /** The Constant PUT. */
    public static final String PUT = "put";

    /** The Constant DELETE. */
    public static final String DELETE = "delete";

    private static final Logger logger = LogManager.getLogger(HttpExecuter.class);

    public static Map<String, Object> execut(String urlString, String username, String password, Integer port, boolean useSSL, String method, Map<String, String> headers,
                                             String requestBody, String authorizationType, String token, boolean getHeaders) throws Exception {
        authorizationType = (authorizationType != null) ? authorizationType : null;
        HttpClient httpClient = HttpClientBuilder.create().build();

        if (useSSL) {
            SSLContext sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[] { new X509TrustManager() {

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            } }, new SecureRandom());
            SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register("https", sslConnectionFactory).build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            httpClient = HttpClients.custom().setConnectionManager(cm).build();
        }

        URL url = new URL(urlString);
        URI uri = null;
        if (port != null) {
            uri = new URI(url.getProtocol(), null, url.getHost(), port, url.getPath(), url.getQuery(), null);
        } else {
            uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
        }

        HttpRequestBase request = null;
        if (method.equals(GET)) {
            request = new HttpGet(uri);
        } else if (method.equals(POST)) {
            request = new HttpPost(uri);
        } else if (method.equals(DELETE)) {
            request = new HttpDelete(uri);
        } else if (method.equals(PUT)) {
            request = new HttpPut(uri);
        }
        if (headers != null) {
            for (String key : headers.keySet()) {
                request.setHeader(key, headers.get(key));
            }
        }
        if (requestBody != null && (method.equals(POST) || method.equals(PUT))) {
            String body = "";
            if (requestBody != null) {
                body = requestBody;
            }
            if (request instanceof HttpPost) {
                ((HttpPost) request).setEntity(new StringEntity(body,"UTF-8"));
            } else {
                ((HttpPut) request).setEntity(new StringEntity(body,"UTF-8"));
            }
        }

        if (authorizationType != null && authorizationType.equals(SystemConstants.Authorization.BASIC)) {
            String encoding = Base64Coder.encodeString(username + ":" + password);
            request.setHeader("Authorization", "Basic " + encoding);
        } else if (authorizationType != null && authorizationType.equals(SystemConstants.Authorization.BEARER)) {
            request.setHeader("Authorization", "Bearer " + token);
        }
        logger.info("***http request: " + uri.toString());
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        Map<String, Object> result = new HashMap<>();
        result.put("headers", response.getAllHeaders());
        result.put("status", response.getStatusLine());
        if (entity != null) {
            InputStream instream = entity.getContent();
            StringWriter writer = new StringWriter();
            //IOUtils.copy(instream, writer, "UTF-8");
            String text = new String(instream.readAllBytes(), StandardCharsets.UTF_8);

            //String val = IOUtils.toString(new InputStreamReader(((Blob) instream).getBinaryStream()));
            result.put("body", text);
            logger.debug("***http response: " + result);
            return result;
        }
        return result;
    }
}
