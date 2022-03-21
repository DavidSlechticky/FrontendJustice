/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.atos.justice.frontend.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * příjem dat k tokenu
 *
 * @author A760135
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenValidationResult {

    boolean valid;
    String role;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "TokenValidationResult{" + "valid=" + valid + ", role=" + role + '}';
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
