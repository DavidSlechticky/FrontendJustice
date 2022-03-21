package net.atos.justice.frontend.rest.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import net.atos.justice.frontend.rest.controller.FrontendRestController;
import net.atos.justice.frontend.rest.model.LogSave;
import net.atos.justice.frontend.rest.model.LogValues;
import net.atos.justice.frontend.rest.model.LogsSearch;
import net.atos.justice.frontend.rest.model.LogsSearchForRequest;
import net.atos.justice.frontend.rest.model.Reason;
import net.atos.justice.frontend.rest.model.SearchRequest;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

/**
 * Audit logging for get PDF file and reasons edit/new
 *
 * @author A760135
 */
public class LogsService {

    private static final Logger log = LoggerFactory.getLogger(FrontendRestController.class);

    private final String saveUrl;
    private final String readUrl;

    @Autowired
    private RestTemplate restTemplate;

    public LogsService(String saveUrl, String readUrl) {
        this.saveUrl = saveUrl;
        this.readUrl = readUrl;

    }

    public LogValues[] getLogsList(String token, LogsSearch logSearchForm) throws ParseException {

        LogsSearchForRequest filtrHledani = new LogsSearchForRequest();

        // string form dates to Date conversion
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (!logSearchForm.getDtfrom().isEmpty()) {
            Date dateFrom = formatter.parse(logSearchForm.getDtfrom());

            int addMinuteTime = 130;
            Date convertedDateFromTime = dateFrom;
            convertedDateFromTime = DateUtils.addMinutes(convertedDateFromTime, addMinuteTime);

            filtrHledani.setDtfrom(convertedDateFromTime);
        }
        if (!logSearchForm.getDtto().isEmpty()) {
            Date dateTo = formatter.parse(logSearchForm.getDtto());

            int addMinuteTime = 130;
            Date convertedDateToTime = dateTo;
            convertedDateToTime = DateUtils.addMinutes(convertedDateToTime, addMinuteTime);

            filtrHledani.setDtto(convertedDateToTime);
        }
        //TO-DO!!! department plnit podle AD uživatele
        filtrHledani.setDepartment(logSearchForm.getDepartment()); // overit, jaky format maji departments
        filtrHledani.setUsername(logSearchForm.getUsername()); // overit, jak vypadaji na Justice user names a podle toho ošetřit vstup https://attacomsian.com/blog/capitalize-first-letter-of-string-java
        filtrHledani.setLogtype(logSearchForm.getLogtype());
        filtrHledani.setDocsign(logSearchForm.getDocsign());

        log.debug("Nacitam seznam logu k prohlizeni s filtrem: " + filtrHledani.toString());
        System.out.println("Nacitam seznam logu k prohlizeni s filtrem: " + filtrHledani.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", token);

        HttpEntity<LogsSearchForRequest> entity = new HttpEntity<LogsSearchForRequest>(filtrHledani, headers);
        ResponseEntity<LogValues[]> responseEntity = restTemplate.exchange(readUrl, HttpMethod.POST, entity, LogValues[].class);

        LogValues[] logValues = responseEntity.getBody();
        return logValues;
    }

    public void saveAuditLog(String token, Reason createReasonRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        LogSave prepareLog = new LogSave();
        prepareLog.setUsername(username);
        //TO-DO!!! department plnit podle AD uživatele
        prepareLog.setDepartment("dočasné oddělení");
        prepareLog.setLogtype("ciselnik");
        prepareLog.setTimestamp(new Date().toString());

        if (createReasonRequest.getId() == 1000) {
            prepareLog.setLogtext("Novy ciselnik: " + createReasonRequest.toString());
            log.debug("Odesilam log k novemu ciselniku do DB" + prepareLog.toString());
            System.out.println("Odesilam log k novemu ciselniku do DB" + prepareLog.toString());
        } else {
            prepareLog.setLogtext("Zmena ciselniku: " + createReasonRequest.toString());
            log.debug("Odesilam log k aktualizaci ciselniku do DB" + prepareLog.toString());
            System.out.println("Odesilam log k aktualizaci ciselniku do DB" + prepareLog.toString());
        }
        ResponseEntity<Object> vysledekLogovani = restTemplate.postForEntity(saveUrl, prepareLog, Object.class);
    }

    public void saveGetPdfAuditLog(SearchRequest getPDFRequest) {
        //UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Object rawPassword = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        LogSave prepareLog = new LogSave();
        prepareLog.setUsername(username);
        prepareLog.setDocsign(getPDFRequest.getZnacka());
        prepareLog.setDepartment(getPDFRequest.getSoud());
        prepareLog.setLogtype("search");
        // prepare log text
        StringBuilder sb = new StringBuilder();
        /*sb.append("Znacka: " + getPDFRequest.getZnacka());
            sb.append(", vybrane sluzby: ");
            for (String hodnota : getPDFRequest.getServiceNames()) {
                sb.append(hodnota + " ");
            }*/
        sb.append(getPDFRequest.toString());
        sb.append(" SearchResponse{}");
        prepareLog.setLogtext(sb.toString());
        prepareLog.setTimestamp(new Date().toString());
        log.debug("Odesilam log k search requestu do DB" + prepareLog.toString());
        System.out.println("Odesilam log k search requestu do DB" + prepareLog.toString());
        ResponseEntity<Object> vysledekLogovani = restTemplate.postForEntity(saveUrl, prepareLog, Object.class);
    }
}
