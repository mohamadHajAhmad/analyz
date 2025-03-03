package sdk.KeycloakSecurity;

import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextListener;
import sdk.model.ServiceExecutionResult;
import sdk.service.TanentService;
import sdk.util.DataHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Component
public class TenantAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

    private static final Logger LOGGER = LogManager.getLogger(TenantAuthenticationManagerResolver.class);
    private final BearerTokenResolver resolver = new DefaultBearerTokenResolver();
    private final Map<String, String> tenants = new HashMap<>();

    private final Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();

    @Value("${keycloak.auth-server-url}")
     private String keycloakUrl;

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }
    @Autowired
    private TanentService tanentService;

    public TenantAuthenticationManagerResolver() {
        //this.tenants.put("SpringBootKeycloak", "http://localhost:8180/auth/realms/SpringBootKeycloak");
        this.tenants.put("cis-p-T102-0000002", "http://192.168.1.74:8080/auth/realms/cis-p-T102-0000002");
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest request) {
        return this.authenticationManagers.computeIfAbsent(toTenant(request), this::fromTenant);
    }

    private String toTenant(HttpServletRequest request) {
        try {
            String token = this.resolver.resolve(request);
            String partnerCode= (String) JWTParser.parse(token).getJWTClaimsSet().getClaim("partnerCode");
            ServiceExecutionResult result = new ServiceExecutionResult();
            String path = getRequestOrigin(request);
            String apiName = getResMappingKey(request.getRequestURI());
            String userName = (String) JWTParser.parse(token).getJWTClaimsSet().getClaim("preferred_username");
            result = tanentService.getPartner(partnerCode);
            if ((!result.isExecutionSuccessful()) || result.getReturnValue() == null) {
                throw new IllegalStateException("Not able to find the partner for the given domain >>>> " + path);
            }
            partnerCode=keycloakUrl+"/realms/"+partnerCode;
            return partnerCode;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private AuthenticationManager fromTenant(String tenant) {
        return Optional.ofNullable(tenant)
                .map(JwtDecoders::fromIssuerLocation)
                .map(jwtDecoder ->new JwtAuthenticationProvider ((JwtDecoder) jwtDecoder) )
                .orElseThrow(() -> new IllegalArgumentException("unknown tenant"))::authenticate;
    }


    /**
     * Gets the request origin.
     *
     * @param request
     *            the request
     * @return the request origin
     */
    private String getRequestOrigin(HttpServletRequest request) {
        try {
            String domain = request.getHeader("referer");
            if (domain == null) {
                domain = request.getHeader("Referer");
            }
            if(domain == null){
                return null;
            }
            String baseUrl = DataHelper.getBaseUrlFromDomain(domain);
            if(baseUrl != null && baseUrl.equalsIgnoreCase("appcenter.intuit.com")){
                baseUrl = null;
            }
            return baseUrl;
        } catch (Exception e) {
            throw new IllegalStateException("Not able to resolve baseURL from the request path!");
        }
    }

    private String getResMappingKey(String requestURL) {
        String resMappingKey = null;
        if (requestURL != null) {
            int index = -1;
            for (int i = requestURL.length() - 1, cnt = 0; i > -1; i--)
                if (requestURL.charAt(i) == '/' && ++cnt == 2) {
                    index = i;
                    break;
                }
            resMappingKey = requestURL.substring(++index);
        }
        return resMappingKey;
    }

}
