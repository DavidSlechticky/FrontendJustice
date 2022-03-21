package net.atos.justice.frontend.rest.service;

import java.util.Arrays;
import net.atos.justice.frontend.rest.controller.FrontendRestController;
import net.atos.justice.frontend.rest.model.Reason;
import net.atos.justice.frontend.rest.model.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author A760135
 */
public class ReasonsService {

    private static final Logger log = LoggerFactory.getLogger(FrontendRestController.class);

    private final String platneUrl;
    private final String vseUrl;
    private final String updateUrl;
    private final String createUrl;
    private final String seznamSluzebUrl;

    @Autowired
    private RestTemplate restTemplate;

    public ReasonsService(String platneUrl, String vseUrl, String updateUrl, String createUrl, String seznamSluzebUrl) {
        this.platneUrl = platneUrl;
        this.vseUrl = vseUrl;
        this.updateUrl = updateUrl;
        this.createUrl = createUrl;
        this.seznamSluzebUrl = seznamSluzebUrl;
    }

    public Reason[] getValidReasons(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", token);
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<Reason[]> platneDuvod = restTemplate.exchange(platneUrl, HttpMethod.GET, entity, Reason[].class);

        Reason[] reasons = platneDuvod.getBody();
        System.out.println("Nacteny platne duvody zadosti na CSSZ" + Arrays.toString(reasons));
        log.info("Nacteny platne duvody zadosti na CSSZ" + Arrays.toString(reasons));
        return reasons;
    }

    public Service[] getAllServices(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", token);
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<Service[]> seznamSluzeb = restTemplate.exchange(seznamSluzebUrl, HttpMethod.GET, entity, Service[].class);
        Service[] services = seznamSluzeb.getBody();
        System.out.println("Seznam sluzeb CSSZ nacten: " + Arrays.toString(services));
        log.debug("Seznam sluzeb CSSZ nacten: " + Arrays.toString(services));
        return services;
    }

    public Reason[] getAllReasons(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", token);
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<Reason[]> allReasons = restTemplate.exchange(vseUrl, HttpMethod.GET, entity, Reason[].class);

        Reason[] reasons = allReasons.getBody();
        System.out.println("Nacteny vsechny duvody zadosti na CSSZ" + Arrays.toString(reasons));
        log.info("Nacteny vsechny duvody zadosti na CSSZ" + Arrays.toString(reasons));
        return reasons;
    }

    public void updateReason(String token, Reason updateReasonRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", token);
        HttpEntity<Reason> entity = new HttpEntity<Reason>(updateReasonRequest, headers);
        restTemplate.exchange(updateUrl + "/" + updateReasonRequest.getId(), HttpMethod.PUT, entity, Reason.class);

        //restTemplate.put(URL_DUVODY_UPDATE + "/" + updateReasonRequest.getId(), updateReasonRequest);
        System.out.println("Zmenen duvod zadosti na CSSZ: " + updateReasonRequest.toString());
        log.info("Zmenen duvod zadosti na CSSZ: " + updateReasonRequest.toString());
    }

    public void createReason(String token, Reason createReasonRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", token);

        HttpEntity<Reason> entity = new HttpEntity<Reason>(createReasonRequest, headers);
        ResponseEntity<Object> vysledekCreateReason = restTemplate.exchange(createUrl, HttpMethod.POST, entity, Object.class);
        System.out.println("Vytvoren novy duvod zadosti na CSSZ" + createReasonRequest.toString());
        log.info("Vytvoren novy duvod zadosti na CSSZ" + createReasonRequest.toString());
    }

}
