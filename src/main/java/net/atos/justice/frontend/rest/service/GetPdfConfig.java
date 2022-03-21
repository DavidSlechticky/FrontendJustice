/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
@ConfigurationProperties("pdf")
public class GetPdfConfig {
    
    @Setter
    private String searchPdfUrl;
    @Setter
    private String fileFolder;
    
    @Bean
    public GetPdfService getSearchPdfUrl() {
        return new GetPdfService(searchPdfUrl, fileFolder);
    }

}

