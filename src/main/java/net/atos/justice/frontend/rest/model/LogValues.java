package net.atos.justice.frontend.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

/**
 * Objekt pro zobrazeni logu uzivateli na HTML logy - není používáno, používá se LogValuesString
 * @author A760135
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogValues {
    private String username;
    private String department;
    private String docsign;
    private String logtype;
    private String logtext;
    private Date timestamp;

    @Override
    public String toString() {
        return "LogValues{" + "username=" + username + ", department=" + department + ", docsign=" + docsign + ", logtype=" + logtype + ", logtext=" + logtext + ", timestamp=" + timestamp + '}';
    }

    public String getDocsign() {
        return docsign;
    }

    public void setDocsign(String docsign) {
        this.docsign = docsign;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public String getLogtext() {
        return logtext;
    }

    public void setLogtext(String logtext) {
        this.logtext = logtext;
    }


    
}
