package net.atos.justice.frontend.rest.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// prehled vsech atributu pro sluzby CSSZ
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attribute implements Serializable {

    private static final long serialVersionUID = 1L;
    private Short id;
    private short inout;
    private String camel;
    private String friendly;
    private short type;
    private String regex;

    @Override
    public String toString() {
        return "Attribute{" + "id=" + id + ", inout=" + inout + ", camel=" + camel + ", friendly=" + friendly + ", type=" + type + ", regex=" + regex + '}';
    }

    public Attribute() {
    }

    public Attribute(Short id) {
        this.id = id;
    }

    public Attribute(Short id, short inout, String friendly, short type, String regex) {
        this.id = id;
        this.inout = inout;
        this.friendly = friendly;
        this.type = type;
        this.regex = regex;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public short getInout() {
        return inout;
    }

    public void setInout(short inout) {
        this.inout = inout;
    }

    public String getCamel() {
        return camel;
    }

    public void setCamel(String camel) {
        this.camel = camel;
    }

    public String getFriendly() {
        return friendly;
    }

    public void setFriendly(String friendly) {
        this.friendly = friendly;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
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
        if (!(object instanceof Attribute)) {
            return false;
        }
        Attribute other = (Attribute) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
