package net.atos.justice.frontend.rest.service;

/**
 *
 * @author A760135
 */
import java.util.ArrayList;
import java.util.List;
import net.atos.justice.frontend.rest.model.TokenValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TokenValidatorService {
    
    private final String tokenUrl;
    
    @Autowired
    private RestTemplate restTemplate;

    public TokenValidatorService(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }
    @Bean
    public boolean hasRole(String token, String role) {
        List<String> rolesForToken = getRolesForToken(token);
        return rolesForToken.contains(role);
    }
    
    public List<String> getRolesForToken(String token) {
        List<String> result = new ArrayList<>();
        ResponseEntity<TokenValidationResult> responseEntitya = restTemplate.getForEntity(tokenUrl + token, TokenValidationResult.class);
        TokenValidationResult tokenResult = responseEntitya.getBody();
        if (tokenResult == null || !tokenResult.isValid()) {
            return result;
        }
        result.add(tokenResult.getRole() != null ? tokenResult.getRole().toUpperCase() : null);
        return result;
    }   
}

