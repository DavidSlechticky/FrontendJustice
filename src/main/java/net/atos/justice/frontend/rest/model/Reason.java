package net.atos.justice.frontend.rest.model;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// seznam duvodu zadosti na CSSZ vracenych z DB
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reason implements Serializable {

    private static final long serialVersionUID = 1L;
    private Short id;
    private String name;
    private Date validfrom;
    private Date validto;
    private String version;

    @Override
    public String toString() {
        return "Reason{" + "id=" + id + ", name=" + name + ", validfrom=" + validfrom + ", validto=" + validto + ", version=" + version + '}';
    }

    public Reason() {
    }

    public Reason(Short id) {
        this.id = id;
    }

    public Reason(Short id, String name, Date validfrom, Date validto, String version) {
        this.id = id;
        this.name = name;
        this.validfrom = validfrom;
        this.validto = validto;
        this.version = version;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getValidfrom() {
        return validfrom;
    }

    public void setValidfrom(Date validfrom) {
        this.validfrom = validfrom;
    }

    public Date getValidto() {
        return validto;
    }

    public void setValidto(Date validto) {
        this.validto = validto;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reason)) {
            return false;
        }
        Reason other = (Reason) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
