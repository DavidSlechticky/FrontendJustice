package net.atos.justice.frontend.rest.service;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author A760135
 */
@Configuration
@ConfigurationProperties("auth")
public class AuthConfig {
    
    @Setter
    private String tokenUrl;
    @Setter
    private String loginUrl;
    
    @Bean
    public TokenValidatorService getTokenValidator() {
        return new TokenValidatorService(tokenUrl);
    }
    
    @Bean
    public CustomAuthenticationProvider getLoginUrl() {
        return new CustomAuthenticationProvider(loginUrl);
    }
}

