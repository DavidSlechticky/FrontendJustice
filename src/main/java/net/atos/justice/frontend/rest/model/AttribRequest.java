package net.atos.justice.frontend.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// vstupy pro odeslani search requestu
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttribRequest {

    private String camel;
    private String value;
    private String friendly;
    private short type;

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AttribRequest{" + "camel=" + camel + ", value=" + value + ", type=" + type  + ", friendly=" + friendly + '}';
    }

    public AttribRequest() {
    }

    public AttribRequest(String camel, String value) {
        this.camel = camel;
        this.value = value;
    }

    public String getCamel() {
        return camel;
    }

    public void setCamel(String camel) {
        this.camel = camel;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFriendly() {
        return friendly;
    }

    public void setFriendly(String friendly) {
        this.friendly = friendly;
    }

}
