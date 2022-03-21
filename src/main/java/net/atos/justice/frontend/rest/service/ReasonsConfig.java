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
@ConfigurationProperties("reason")
public class ReasonsConfig {
    
    @Setter
    private String platneUrl;
    @Setter
    private String vseUrl;
    @Setter
    private String updateUrl;
    @Setter
    private String createUrl;
    @Setter
    private String seznamSluzebUrl;
    
    @Bean
    public ReasonsService getUrl() {
        return new ReasonsService(platneUrl, vseUrl,updateUrl, createUrl, seznamSluzebUrl);
    }
}
