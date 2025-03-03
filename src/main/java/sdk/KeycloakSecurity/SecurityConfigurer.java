package sdk.KeycloakSecurity;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer   {

    private static final Logger logger = LogManager.getLogger(SecurityConfigurer.class);


    private final KeycloakLogoutHandler keycloakLogoutHandler;


    @Autowired
    private AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

    SecurityConfigurer(KeycloakLogoutHandler keycloakLogoutHandler) {
        this.keycloakLogoutHandler = keycloakLogoutHandler;
    }


    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        http.cors()
//                .and()
//                .authorizeRequests().requestMatchers("/api/**")
//                .authenticated().anyRequest().permitAll()
//                .and()
//                .oauth2ResourceServer().authenticationManagerResolver(this.authenticationManagerResolver);
//

        http. authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/**") .authenticated()
                .anyRequest().permitAll()).oauth2ResourceServer(o->o.authenticationManagerResolver(this.authenticationManagerResolver));
        http.oauth2Login()
                .and()
                .logout()
                .addLogoutHandler(keycloakLogoutHandler)
                .logoutSuccessUrl("/");
        return http.build();

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/app/**");
    }

}
