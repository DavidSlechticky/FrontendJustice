package net.atos.justice.frontend.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

/**
 * Log modul potřebuje vstupní formát dtfrom a dtto jako date
 * @author A760135
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogsSearchForRequest {

    private String username;
    private String department;
    private String docsign;
    private String logtype;
    private Date dtfrom;
    private Date dtto;

    @Override
    public String toString() {
        return "LogsSearchForRequest{" + "username=" + username + ", department=" + department + ", docsign=" + docsign + ", logtype=" + logtype + ", dtfrom=" + dtfrom + ", dtto=" + dtto + '}';
    }

    public String getDocsign() {
        return docsign;
    }

    public void setDocsign(String docsign) {
        this.docsign = docsign;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype;
    }

    public Date getDtfrom() {
        return dtfrom;
    }

    public void setDtfrom(Date dtfrom) {
        this.dtfrom = dtfrom;
    }

    public Date getDtto() {
        return dtto;
    }

    public void setDtto(Date dtto) {
        this.dtto = dtto;
    }
    
}
