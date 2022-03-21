package net.atos.justice.frontend.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * pro validaci formuláře entries
 *
 * @author A760135
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchFormValidation {

    //@NotEmpty(message = "Značka nesmí být prázdná")
    @Size(min = 2, message = "Značka musí obsahovat minimálně dva znaky")
    public String znacka;
    //@NotEmpty(message = "Soud nesmí být prázdný")
    public String soud;
    public String duvod;
    public String uzivatel;
    public String datumDotazu;
    public String druhDavky;
    //@NotEmpty(message = "Jméno nesmí být prázdné")
    public String jmeno;
    //@Pattern(regexp = "^[a-zA-Z0-9]{6}", message = "Rodné číslo musí mít 6 znaků")
    public String rodneCislo;
    public String prijmeni;
    public String rodnePrijmeni;
    public String datumNarozeni;
    public String obdobiVypisuOd;
    public String obdobiVypisuDo;
    public String pouzeOtevreneVztahy;
    public String ico;
    public String vs;
    public String stat;
    public String zobrazitCisloUctuAdresuVyplaceni;

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

    public String getDatumDotazu() {
        return datumDotazu;
    }

    public void setDatumDotazu(String datumDotazu) {
        this.datumDotazu = datumDotazu;
    }

    public String getDruhDavky() {
        return druhDavky;
    }

    public void setDruhDavky(String druhDavky) {
        this.druhDavky = druhDavky;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getRodneCislo() {
        return rodneCislo;
    }

    public void setRodneCislo(String rodneCislo) {
        this.rodneCislo = rodneCislo;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public String getRodnePrijmeni() {
        return rodnePrijmeni;
    }

    public void setRodnePrijmeni(String rodnePrijmeni) {
        this.rodnePrijmeni = rodnePrijmeni;
    }

    public String getDatumNarozeni() {
        return datumNarozeni;
    }

    public void setDatumNarozeni(String datumNarozeni) {
        this.datumNarozeni = datumNarozeni;
    }

    public String getObdobiVypisuOd() {
        return obdobiVypisuOd;
    }

    public void setObdobiVypisuOd(String obdobiVypisuOd) {
        this.obdobiVypisuOd = obdobiVypisuOd;
    }

    public String getObdobiVypisuDo() {
        return obdobiVypisuDo;
    }

    public void setObdobiVypisuDo(String obdobiVypisuDo) {
        this.obdobiVypisuDo = obdobiVypisuDo;
    }

    public String getPouzeOtevreneVztahy() {
        return pouzeOtevreneVztahy;
    }

    public void setPouzeOtevreneVztahy(String pouzeOtevreneVztahy) {
        this.pouzeOtevreneVztahy = pouzeOtevreneVztahy;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getVs() {
        return vs;
    }

    public void setVs(String vs) {
        this.vs = vs;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getZobrazitCisloUctuAdresuVyplaceni() {
        return zobrazitCisloUctuAdresuVyplaceni;
    }

    public void setZobrazitCisloUctuAdresuVyplaceni(String zobrazitCisloUctuAdresuVyplaceni) {
        this.zobrazitCisloUctuAdresuVyplaceni = zobrazitCisloUctuAdresuVyplaceni;
    }

}
