package net.atos.justice.frontend.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * for external autentization
 * @author A760135
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetail {
    private boolean valid;
    private String status;
    private Uzivatel uzivatel;

    @Override
    public String toString() {
        return "UserDetail{" + "valid=" + valid + ", status=" + status + ", uzivatel=" + uzivatel + '}';
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Uzivatel getUzivatel() {
        return uzivatel;
    }

    public void setUzivatel(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
    }
    
}
