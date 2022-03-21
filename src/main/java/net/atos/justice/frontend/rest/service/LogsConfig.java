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
@ConfigurationProperties("logs")
public class LogsConfig {
    
    @Setter
    private String saveUrl;
    @Setter
    private String readUrl;
    
    @Bean
    public LogsService getLogUrl() {
        return new LogsService(saveUrl, readUrl);
    }
}
