package sdk.KeycloakSecurity;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfiguration {

    // First Method: default
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    // Second Method: Using RestTemplateBuilder
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
