package net.atos.justice.frontend.rest.model;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// přehled služeb vracených z DB pro volbu na sluzby.html
@JsonIgnoreProperties(ignoreUnknown = true)
public class Service implements Serializable {

    private static final long serialVersionUID = 1L;
    private short id;
    private String name;
    private String friendly;
    private String description;
    private String idtext;
    private List<Attribute> attributesInput;
    private List<Attribute> attributesOutput;

    @Override
    public String toString() {
        return "Service{" + "id=" + id + ", name=" + name + ", friendly=" + friendly + ", description=" + description + ", idtext=" + idtext + ", attributesInput=" + attributesInput + ", attributesOutput=" + attributesOutput + '}';
    }

    public Service() {
    }

    public Service(String idtext) {
        this.idtext = idtext;
    }

    public Service(String idtext, short id, String name, String friendly, String description) {
        this.idtext = idtext;
        this.id = id;
        this.name = name;
        this.friendly = friendly;
        this.description = description;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriendly() {
        return friendly;
    }

    public void setFriendly(String friendly) {
        this.friendly = friendly;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdtext() {
        return idtext;
    }

    public void setIdtext(String idtext) {
        this.idtext = idtext;
    }

    public List<Attribute> getAttributesInput() {
        return attributesInput;
    }

    public void setAttributesInput(List<Attribute> attributesInput) {
        this.attributesInput = attributesInput;
    }

    public List<Attribute> getAttributesOutput() {
        return attributesOutput;
    }

    public void setAttributesOutput(List<Attribute> attributesOutput) {
        this.attributesOutput = attributesOutput;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtext != null ? idtext.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Service)) {
            return false;
        }
        Service other = (Service) object;
        if ((this.idtext == null && other.idtext != null) || (this.idtext != null && !this.idtext.equals(other.idtext))) {
            return false;
        }
        return true;
    }
}
