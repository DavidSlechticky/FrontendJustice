package net.atos.justice.frontend.rest.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

// tento objekt se plni pri zadosti o PDF smerem na search engine
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRequest {
    private String znacka;
    private String soud;
    private String duvod;
    private String uzivatel;
    private Date datumDotazu;
    
    private List<String> serviceNames;
    private List<AttribRequest> inputValues;

    @Override
    public String toString() {
        return "SearchRequest{" + "znacka=" + znacka + ", soud=" + soud + ", duvod=" + duvod + ", uzivatel=" + uzivatel + ", datumDotazu=" + datumDotazu + ", serviceNames=" + serviceNames + ", inputValues=" + inputValues + '}';
    }

    public SearchRequest() {
    }

    public String getZnacka() {
        return znacka;
    }

    public void setZnacka(String znacka) {
        this.znacka = znacka;
    }

    public String getSoud() {
        return soud;
    }

    public void setSoud(String soud) {
        this.soud = soud;
    }

    public String getDuvod() {
        return duvod;
    }

    public void setDuvod(String duvod) {
        this.duvod = duvod;
    }

    public String getUzivatel() {
        return uzivatel;
    }

    public void setUzivatel(String uzivatel) {
        this.uzivatel = uzivatel;
    }

    public Date getDatumDotazu() {
        return datumDotazu;
    }

    public void setDatumDotazu(Date datumDotazu) {
        this.datumDotazu = datumDotazu;
    }
    
    public List<String> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(List<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public List<AttribRequest> getInputValues() {
        return inputValues;
    }

    public void setInputValues(List<AttribRequest> inputValues) {
        this.inputValues = inputValues;
    }

}
