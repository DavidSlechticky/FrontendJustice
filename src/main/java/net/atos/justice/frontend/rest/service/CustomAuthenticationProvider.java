package net.atos.justice.frontend.rest.service;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import net.atos.justice.frontend.rest.controller.FrontendRestController;
import net.atos.justice.frontend.rest.model.UserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * Slouží pro vlastní externí autentizaci
 *
 * @author A760135
 */
@Primary
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final String loginUrl;

    @Autowired
    RestTemplate restTemplate;

    public CustomAuthenticationProvider(String loginUrl) {
        this.loginUrl = loginUrl;
    }
    private static final Logger log = LoggerFactory.getLogger(FrontendRestController.class);

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetail userDetail = new UserDetail();
        String fullUrl = loginUrl + "/login?login=" + name + "&password=" + password;
        try {
            ResponseEntity<UserDetail> responseEntityForLogin = restTemplate.getForEntity(fullUrl, UserDetail.class);
            userDetail = responseEntityForLogin.getBody();
        } catch (RestClientException rce) {
            log.warn("Technicka chyba API rozhrani: " + rce);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Technicka chyba API rozhrani", rce);
        } catch (AuthenticationException auex) {
            log.warn("Technicka chyba autentikace: " + auex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Technicka chyba autentikace", auex);
        } catch (Exception ex) {
            log.warn("Obecna chyba autorizace: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Obecna chyba autorizace", ex);
        }

        if (userDetail == null) {
            throw new UsernameNotFoundException(String.format("Neplatné přihl. údaje ", authentication.getPrincipal()));
        } else if (!userDetail.isValid()) {
            throw new BadCredentialsException("Neplatné údaje");
        }

        /*List<String> role = new ArrayList<>();
        if (userDetail != null && userDetail.getUzivatel() != null && userDetail.getUzivatel().getRole() != null) {
            role.add(userDetail.getUzivatel().getRole());
        }*/
        // ulozeni tokenu do session
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);
        session.setAttribute("token", userDetail.getUzivatel().getToken());

        // doplneni role
        List<SimpleGrantedAuthority> authorities = null;
        authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(/*"ROLE_" + */userDetail.getUzivatel().getRole().toUpperCase()));

        return new UsernamePasswordAuthenticationToken(userDetail.getUzivatel().getJmeno(), password, authorities); //authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
