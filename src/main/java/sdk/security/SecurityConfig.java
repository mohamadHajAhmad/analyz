//package sdk.security;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//
//    private  final String[] PUBLIC_POINT= {"/v2/**"} ;
//
//    @Autowired
//    private AuthEntryPointJwt unauthorizedHandler;
//
//    @Autowired
//    UserService userDetailsService;
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//
//        return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userDetailsService).and().build();
//
//    }
//
//    @Bean
//    AuthFilter authFilter() {
//        return new AuthFilter();
//    }
//
////    @Bean
////    public InMemoryUserDetailsManager userDetailsService() {
////
////        // add {noop} before passord to NoOpPasswordEncoder.
////        UserDetails user = User.withUsername("user")
////                .password("{noop}password")
////                .roles("USER")
////                .build();
////
////        UserDetails admin = User.withUsername("admin")
////                .password("{noop}admin")
////                .roles("USER", "ADMIN")
////                .build();
////
////        UserDetails user2 = User.withUsername("user2")
////                .password("{noop}password")
////                .roles("USER")
////                .build();
////
////        return new InMemoryUserDetailsManager(user,user2 ,admin);
////    }
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http.authorizeRequests()
////                .requestMatchers("/v1/api/account")
////                .authenticated()
////                .and()
////                .httpBasic();
//////http.authorizeRequests()
////                .requestMatchers("/v1/api/account/**").hasRole("USER").anyRequest().permitAll().and().httpBasic();
//
////        http.authorizeRequests()
////                .requestMatchers("/v1/api/account").hasRole("USER")
////                .and()
////                .httpBasic();
//
////        http.cors().and().csrf().disable()
////                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
////                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
////                .authorizeRequests().requestMatchers(PUBLIC_POINT).permitAll()
////                .anyRequest().authenticated().and().addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests().requestMatchers(PUBLIC_POINT).permitAll()
//                .anyRequest().authenticated().and().addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
//
//
//
//
////        return http.build();
//
//      return http.build();
//    }
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/api/v1/auth/**");
//    }
//
//
//}
