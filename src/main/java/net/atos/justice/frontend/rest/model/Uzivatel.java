package net.atos.justice.frontend.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * for externa autentization
 * @author A760135
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Uzivatel {
    private String token;
    private String jmeno;
    private String role;

    @Override
    public String toString() {
        return "Uzivatel{" + "token=" + token + ", jmeno=" + jmeno + ", role=" + role + '}';
    }
    

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
