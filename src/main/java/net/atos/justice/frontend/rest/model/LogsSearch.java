package net.atos.justice.frontend.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

/**
 * slouzi pro vyhledavani v DB logu
 *
 * @author A760135
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogsSearch {

    private String username;
    private String department;
    private String docsign;
    private String logtype;
    private String dtfrom;
    private String dtto;

    @Override
    public String toString() {
        return "LogsSearch{" + "username=" + username + ", department=" + department + ", docsign=" + docsign + ", logtype=" + logtype + ", dtfrom=" + dtfrom + ", dtto=" + dtto + '}';
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

    public String getDtfrom() {
        return dtfrom;
    }

    public void setDtfrom(String dtfrom) {
        this.dtfrom = dtfrom;
    }

    public String getDtto() {
        return dtto;
    }

    public void setDtto(String dtto) {
        this.dtto = dtto;
    }
}
