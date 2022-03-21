package net.atos.justice.frontend.rest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import net.atos.justice.frontend.rest.model.AttribRequest;
import net.atos.justice.frontend.rest.model.Attribute;
import net.atos.justice.frontend.rest.model.LogSave;
import net.atos.justice.frontend.rest.model.LogValues;
import net.atos.justice.frontend.rest.model.LogsSearch;
import net.atos.justice.frontend.rest.model.LogsSearchForRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import net.atos.justice.frontend.rest.model.Reason;
import net.atos.justice.frontend.rest.model.ReasonString;
import net.atos.justice.frontend.rest.model.SearchFormValidation;
import net.atos.justice.frontend.rest.model.SearchRequest;
import net.atos.justice.frontend.rest.model.Service;
import net.atos.justice.frontend.rest.service.GetPdfService;
import net.atos.justice.frontend.rest.service.LogsService;
import net.atos.justice.frontend.rest.service.ReasonsService;
import net.atos.justice.frontend.rest.service.TokenValidatorService;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class FrontendRestController {

    private static final Logger log = LoggerFactory.getLogger(FrontendRestController.class);

    private Reason[] reasons;
    private Reason[] reasonsVse;
    private Service[] services;
    private SearchRequest searchRequestFirst = new SearchRequest();
    private SearchRequest searchRequest = new SearchRequest();
    private List<String> vybraneSluzby = new ArrayList<String>();
    private List<Attribute> formValues = new ArrayList();
    private SearchRequest getPDFRequest = new SearchRequest();
    private SearchFormValidation searchFormValidation = new SearchFormValidation();
    private LogsSearch logSearchForm = new LogsSearch();
    private String role;
    private String fileNameAndPath;
    private String fileName;

    // rest API base method
    @Autowired
    RestTemplate restTemplate;

    // token validation
    @Autowired
    private TokenValidatorService tokenValidator;

    // reason management
    @Autowired
    private ReasonsService reasonService;

    // log management
    @Autowired
    private LogsService logService;

    // get PDF file
    @Autowired
    private GetPdfService getPdfService;

    // home page home.html
    @GetMapping("/")
    public String main(Authentication authentication, HttpSession session) {

        if (authentication != null) {
            this.role = authentication.getAuthorities().toString();
        } else {
            this.role = "";
        }
        session.setAttribute("role", role);
        return "home"; //view
    }

    // page for design tests test.html
    @GetMapping("/test")
    public String test(Authentication authentication, HttpSession session) {

        if (authentication != null) {
            this.role = authentication.getAuthorities().toString();
        } else {
            this.role = "";
        }
        session.setAttribute("role", role);
        return "test"; //view
    }

    // log overview and filtering logy.html
    @GetMapping("/logy")
    public String logy(Authentication authentication, Model model, HttpSession session) {
        model.addAttribute("logSearchForm", this.logSearchForm);
        if (authentication != null) {
            this.role = authentication.getAuthorities().toString();
        } else {
            this.role = "";
        }

        // verify token validity, logout user if token not valid
        String token = (String) session.getAttribute("token");
        try {
            if (!tokenValidator.hasRole(token, "ADMIN") && !tokenValidator.hasRole(token, "SUPER-USER")) {
                System.out.println("Neplatny token, odhlasuji uzivatele");
                log.warn("Neplatny token, odhlasuji uzivatele: " + token);
                this.role = "";
                session.setAttribute("role", role);
                SecurityContextHolder.getContext().setAuthentication(null);
                return "redirect:login";
            }
        } catch (Exception ex) {
            log.warn("Platnost tokenu se nepodarilo overit: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Platnost tokenu se nepodarilo overit: ", ex);
        }

        session.setAttribute("role", role);
        return "logy"; //view
    }

    // submit (Vyhledat) in logy.hml
    @PostMapping(value = "/searchLog")
    public String createRequest(@RequestBody MultiValueMap<String, String> formSearchLog, @ModelAttribute LogsSearch logSearchForm, BindingResult result, Model model, HttpSession session) throws ParseException {
        model.addAttribute("logSearchForm", logSearchForm);
        model.addAttribute("formData", formSearchLog);
        LogsSearchForRequest filtrHledani = new LogsSearchForRequest();

        // verify token validity, logout user if token not valid
        String token = (String) session.getAttribute("token");
        try {
            if (!tokenValidator.hasRole(token, "ADMIN") && !tokenValidator.hasRole(token, "SUPER-USER")) {
                System.out.println("Neplatny token, odhlasuji uzivatele");
                log.warn("Neplatny token, odhlasuji uzivatele: " + token);
                this.role = "";
                session.setAttribute("role", role);
                SecurityContextHolder.getContext().setAuthentication(null);
                return "redirect:login";
            }
        } catch (Exception ex) {
            log.warn("Platnost tokenu se nepodarilo overit: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Platnost tokenu se nepodarilo overit: ", ex);
        }
        // get logs
        try {
            LogValues[] logValues = logService.getLogsList(token, logSearchForm);
            model.addAttribute("logValues", logValues);
            return "logy";

        } catch (Exception ex) {
            log.warn("Seznam logu k prohlizeni s filtrem se nepodarilo nacist: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Seznam logu k prohlizeni s filtrem se nepodarilo nacist: ", ex);

        }
        //int statusCode = readLogsResult.getStatusCodeValue();
    }

    // load list of reasons (duvody zadosti ciselnik)
    @GetMapping("/ciselniky")
    public String ciselniky(Authentication authentication, Model model, HttpSession session) {
        model.addAttribute("reasons", reasons);
        model.addAttribute("services", services);

        if (authentication != null) {
            this.role = authentication.getAuthorities().toString();
        } else {
            this.role = "";
        }
        session.setAttribute("role", role);

        // verify token validity, logout user if token not valid
        String token = (String) session.getAttribute("token");
        try {
            if (!tokenValidator.hasRole(token, "ADMIN")) {
                System.out.println("Neplatny token, odhlasuji uzivatele");
                log.warn("Neplatny token, odhlasuji uzivatele: " + token);
                this.role = "";
                session.setAttribute("role", role);
                SecurityContextHolder.getContext().setAuthentication(null);
                return "redirect:login";
            }
        } catch (Exception ex) {
            log.warn("Platnost tokenu se nepodarilo overit: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Platnost tokenu se nepodarilo overit: ", ex);
        }
        // read list of reasons from DB via API
        try {
            this.reasonsVse = null;
            this.reasonsVse = reasonService.getAllReasons(token);
            model.addAttribute("reasonsVse", reasonsVse);
            Reason duvodyForm = new Reason();
            model.addAttribute("duvodyForm", duvodyForm);
            return "ciselniky"; //view ciselniky.html

        } catch (Exception ex) {
            log.warn("Seznam ciselniku k editaci se nepodarilo nacist: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Seznam ciselniku k editaci se nepodarilo nacist: ", ex);
        }
    }

    // after pressing button "Změnit" in ciselniky.html
    @RequestMapping(value = "/reasonUpdateEdit", method = RequestMethod.POST)
    public String reasonUpdateEdit(@ModelAttribute ReasonString reason, Model model) throws ParseException {

        model.addAttribute("reason", reason);
        return "zmenaciselniku"; //view zmenaciselniku.html
    }

    // reason update (pressed "Změnit")
    @RequestMapping("/reasonUpdate")
    public String reasonUpdate(@ModelAttribute ReasonString reasonString, Model model, HttpSession session) throws ParseException {

        // convert form string date to Date & prepare request
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDateFrom = sdf.parse(reasonString.getValidfrom());
        Date convertedDateTo = sdf.parse(reasonString.getValidto());
        Reason updateReasonRequest = new Reason();
        updateReasonRequest.setValidfrom(convertedDateFrom);
        updateReasonRequest.setValidto(convertedDateTo);
        updateReasonRequest.setId(new Short(reasonString.getId()));
        updateReasonRequest.setName(reasonString.getName());
        updateReasonRequest.setVersion(reasonString.getVersion());

        // verify token validity, logout user if token not valid
        String token = (String) session.getAttribute("token");
        try {
            if (!tokenValidator.hasRole(token, "ADMIN")) {
                System.out.println("Neplatny token, odhlasuji uzivatele");
                log.warn("Neplatny token, odhlasuji uzivatele: " + token);
                this.role = "";
                session.setAttribute("role", role);
                SecurityContextHolder.getContext().setAuthentication(null);
                return "redirect:login";
            }
        } catch (Exception ex) {
            log.warn("Platnost tokenu se nepodarilo overit: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Platnost tokenu se nepodarilo overit: ", ex);
        }

        // do update of reason
        try {
            reasonService.updateReason(token, updateReasonRequest);
        } catch (Exception ex) {
            log.warn("Nepodarilo se zaktualizovat ciselnik duvodu: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Ciselnik se nepodarilo zaktualizovat", ex);
        }

        // log info about reason change to audit log
        try {
            logService.saveAuditLog(token, updateReasonRequest);
        } catch (Exception ex) {
            log.warn("Nepodarilo se zalogovat zmenu duvodu: " + ex);
        }
        String ciselnikZmenen = "Důvod žádosti byl změněn.";
        session.setAttribute("ciselnikZmenen", ciselnikZmenen);
        return "redirect:ciselniky"; //view ciselniky.html
    }

    // after pressing button "Nový číselník" in header
    @RequestMapping(value = "/reasonNewEdit", method = RequestMethod.GET)
    public String reasonNewEdit(Model model, HttpSession session) {
        Reason reasonNew = new Reason();
        model.addAttribute("reasonNew", reasonNew);

        // verify token validity, logout user if token not valid
        String token = (String) session.getAttribute("token");
        try {
            if (!tokenValidator.hasRole(token, "ADMIN")) {
                System.out.println("Neplatny token, odhlasuji uzivatele");
                log.warn("Neplatny token, odhlasuji uzivatele: " + token);
                this.role = "";
                session.setAttribute("role", role);
                SecurityContextHolder.getContext().setAuthentication(null);
                return "redirect:login";
            }
        } catch (Exception ex) {
            log.warn("Platnost tokenu se nepodarilo overit: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Platnost tokenu se nepodarilo overit: ", ex);
        }
        return "novyciselnik"; // view novyciselnik.html
    }

    // create new reason (pressed "Vytvořit")
    @RequestMapping("/reasonNew")
    public String reasonNew(@ModelAttribute ReasonString reasonNew, Model model, HttpSession session) throws ParseException {

        // convert form string date to Date & prepare request
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDateFrom = sdf.parse(reasonNew.getValidfrom());
        Date convertedDateTo = sdf.parse(reasonNew.getValidto());
        Reason createReasonRequest = new Reason();
        createReasonRequest.setValidfrom(convertedDateFrom);
        createReasonRequest.setValidto(convertedDateTo);
        Integer sh_object = new Integer(1000);
        short sh_value = sh_object.shortValue();
        createReasonRequest.setId(sh_value);
        createReasonRequest.setName(reasonNew.getName());
        createReasonRequest.setVersion("0");

        // verify token validity, logout user if token not valid
        String token = (String) session.getAttribute("token");
        try {
            if (!tokenValidator.hasRole(token, "ADMIN")) {
                System.out.println("Neplatny token, odhlasuji uzivatele");
                log.warn("Neplatny token, odhlasuji uzivatele: " + token);
                this.role = "";
                session.setAttribute("role", role);
                SecurityContextHolder.getContext().setAuthentication(null);
                return "redirect:login";
            }
        } catch (Exception ex) {
            log.warn("Platnost tokenu se nepodarilo overit: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Platnost tokenu se nepodarilo overit: ", ex);
        }

        // do create new reason
        try {
            reasonService.createReason(token, createReasonRequest);

        } catch (AuthenticationException auex) {
            log.warn("Technicka chyba autentikace: " + auex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Technicka chyba autentikace", auex);
        } catch (RestClientException rce) {
            log.warn("Technicka chyba API rozhrani: " + rce);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Technicka chyba API rozhrani", rce);

        } catch (Exception ex) {
            log.warn("Nepodarilo se vytvorit novy ciselnik duvodu: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Nepodarilo se vytvorit novy ciselnik duvodu", ex);
        }

        // log info about new reason to audit log
        try {
            logService.saveAuditLog(token, createReasonRequest);
        } catch (Exception ex) {
            log.warn("Nepodarilo se zalogovat vytvoreni noveho duvodu: " + ex);
        }
        String ciselnikZmenen = "Důvod žádosti byl změněn.";
        session.setAttribute("ciselnikZmenen", ciselnikZmenen);
        return "redirect:ciselniky";
    }

    // show napoveda.html
    @GetMapping("/napoveda")
    public String napoveda(Model model) {
        model.addAttribute("reasons", reasons);
        model.addAttribute("services", services);

        return "napoveda"; //view napoveda.html
    }

    // do login
    @GetMapping("/login")
    public String login(Model model) {

        return "login"; //view
    }

    // do logout
    @GetMapping("/logout")
    public String logout(Model model) {

        return "logout"; //view
    }

    // load page sluzby.html
    @GetMapping("/sluzby")
    public String sluzby(Authentication authentication, Model model, HttpSession session) {
        model.addAttribute("reasons", reasons);
        model.addAttribute("services", services);
        model.addAttribute("searchRequestFirst", searchRequestFirst);

        if (authentication != null) {
            this.role = authentication.getAuthorities().toString();
        } else {
            this.role = "";
        }
        session.setAttribute("role", role);

        // verify token validity, logout user if token not valid
        String token = (String) session.getAttribute("token");
        try {
            if (!tokenValidator.hasRole(token, "ADMIN")
                    && !tokenValidator.hasRole(token, "USER")
                    && !tokenValidator.hasRole(token, "SUPER-USER")) {
                System.out.println("Neplatny token, odhlasuji uzivatele");
                log.warn("Neplatny token, odhlasuji uzivatele: " + token);
                this.role = "";
                session.setAttribute("role", role);
                SecurityContextHolder.getContext().setAuthentication(null);
                return "redirect:login";
            }
        } catch (Exception ex) {
            log.warn("Platnost tokenu se nepodarilo overit: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Platnost tokenu se nepodarilo overit: ", ex);
        }

        // load actual list of reasons a services list
        try {
            this.reasons = reasonService.getValidReasons(token);
            this.services = reasonService.getAllServices(token);
            model.addAttribute("reasons", this.reasons);
            model.addAttribute("services", this.services);
            return "sluzby"; //view sluzby.html

        } catch (Exception ex) {
            log.warn("Nepodarilo se nacist ciselniky: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Nepodarilo se nacist ciselniky: ", ex);
        }
    }

    // load vstupy.html
    @GetMapping("/vstupy")
    public String vstupy(Model model) {
        model.addAttribute("reasons", reasons);
        model.addAttribute("services", services);
        model.addAttribute("searchRequest", searchRequest);
        model.addAttribute("searchRequestFirst", searchRequestFirst);

        return "sluzby"; //view vstupy.html
    }

    // result page vysledek.html
    @GetMapping("/vysledek")
    public String odeslat(Model model) {
        model.addAttribute("reasons", reasons);
        model.addAttribute("services", services);
        model.addAttribute("searchRequest", searchRequest);
        model.addAttribute("getPDFRequest", getPDFRequest);
        model.addAttribute("fileNameAndPath", this.fileNameAndPath);
        model.addAttribute("fileName", this.fileName);

        return "vysledek"; //view vysledek.html
    }

    // recap screen shrnuti.html - after click on "Dokončit", sends search requests and receives PDF file
    @PostMapping("/submit")
    public String submitRequest(Model model, Authentication authentication, HttpSession session) throws IOException {

        // verify token validity, logout user if token not valid
        String token = (String) session.getAttribute("token");
        try {
            if (!tokenValidator.hasRole(token, "ADMIN")
                    && !tokenValidator.hasRole(token, "USER")
                    && !tokenValidator.hasRole(token, "SUPER-USER")) {
                System.out.println("Neplatny token, odhlasuji uzivatele");
                log.warn("Neplatny token, odhlasuji uzivatele: " + token);
                this.role = "";
                session.setAttribute("role", role);
                SecurityContextHolder.getContext().setAuthentication(null);
                return "redirect:login";
            }
        } catch (Exception ex) {
            log.warn("Platnost tokenu se nepodarilo overit: " + ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Platnost tokenu se nepodarilo overit: ", ex);
        }
        // sending requestu to search engine - expecting PDF file on return
        try {
            this.fileNameAndPath = "";
            this.fileNameAndPath = getPdfService.getPdfFile(token, getPDFRequest);
            this.fileName = this.fileNameAndPath.substring(this.fileNameAndPath.lastIndexOf('/') + 1).trim();
            model.addAttribute("fileName", fileName);

            return "redirect:vysledek";

        } catch (Exception ex) {
            log.warn("Nepodarilo se nacist PDF: " + ex);
            // log search requestu values in case of request failure
            try {
                logService.saveGetPdfAuditLog(getPDFRequest);
            } catch (Exception exc) {
                log.warn("Nepodarilo se zalogovat zadost o PDF: " + exc);
            }
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Nepodarilo se nacist PDF: ", ex);
        }
    }

    // download PDF file on users request
    @RequestMapping(method = {RequestMethod.GET}, value = {"/downloadPdf"})
    public ResponseEntity<InputStreamResource> downloadPdf() {
        String path = fileNameAndPath;

        File file = new File(path);
        try {
            HttpHeaders respHeaders = new HttpHeaders();
            MediaType mediaType = MediaType.parseMediaType("application/pdf");
            respHeaders.setContentType(mediaType);
            respHeaders.setContentLength(file.length());
            respHeaders.setContentDispositionFormData("attachment", file.getName());
            InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
            file.delete();
            return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
        } catch (Exception exc) {
            String message = "Chyba při stažení souboru " + file + exc.getMessage();
            log.error(message, exc);
            return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // back from recap screen shrnuti.html to change values in form
    @GetMapping("/odeslatform")
    public String zpetZShrnuti(Model model) {
        model.addAttribute("reasons", reasons);
        model.addAttribute("services", services);
        model.addAttribute("searchRequest", searchRequest);
        model.addAttribute("getPDFRequest", getPDFRequest);
        model.addAttribute("formValues", formValues);
        model.addAttribute("vybraneSluzby", vybraneSluzby);
        model.addAttribute("searchRequestFirst", searchRequestFirst);
        model.addAttribute("searchFormValidation", this.searchFormValidation);

        return "vstupy"; //view vstupy.html
    }

    // submit (Pokracovat) in vstupy.html with values validation https://stackabuse.com/spring-boot-thymeleaf-form-data-validation-with-bean-validator/
    @PostMapping(value = "/odeslatform")
    public String createRequest(@RequestBody MultiValueMap<String, String> formData, @Valid SearchFormValidation searchFormValidation, BindingResult result, Model model) throws ParseException {
        model.addAttribute("reasons", reasons);
        model.addAttribute("services", services);
        model.addAttribute("searchRequest", searchRequest);
        model.addAttribute("vybraneSluzby", vybraneSluzby);
        model.addAttribute("formValues", formValues);
        model.addAttribute("formData", formData);
        this.searchFormValidation = searchFormValidation;
        model.addAttribute("searchFormValidation", this.searchFormValidation);
        if (result.hasErrors()) {
            return "vstupy"; // view vstupy.html to fix errors
        }
        getPDFRequest = createGetPDFRequest(vybraneSluzby, formData, services, formValues);
        model.addAttribute("getPDFRequest", getPDFRequest);
        return "shrnuti"; // view shrnuti.html
    }

    // click on "Pokračovat" on sluzby.html form
    @PostMapping(value = "/vstupy")
    public String testCheckboxes(@ModelAttribute SearchRequest searchRequestFirst, Model model) {
        model.addAttribute("reasons", reasons);
        model.addAttribute("services", services);
        model.addAttribute("searchRequest", searchRequest);
        model.addAttribute("searchRequestFirst", searchRequestFirst);
        List<String> vysledek = searchRequestFirst.getServiceNames();
        vybraneSluzby.clear();
        boolean isvybraneSluzbyEmpty = true;
        for (String vysl : vysledek) {
            if (vysl != null && vysl.length() > 0) {
                vybraneSluzby.add(vysl);
                isvybraneSluzbyEmpty = false;
            }
        }
        // check if at least one service was selected
        if (!isvybraneSluzbyEmpty) {
            formValues.clear();
            formValues = prepareFormValues(vybraneSluzby, services);
            model.addAttribute("formValues", formValues);
            model.addAttribute("vybraneSluzby", vybraneSluzby);
            searchFormValidation = new SearchFormValidation();
            model.addAttribute("searchFormValidation", searchFormValidation);
            return "vstupy"; // view vstupy.html
        } else {
            return "sluzby"; // user must select at least one service
        }
    }

    // based on selected services prepare form inputs to be filled by user
    List<Attribute> prepareFormValues(List<String> vybraneSluzby, Service[] services) {
        List<Attribute> result = new ArrayList();

        for (Service service : services) {
            for (String vybranaSluzba : vybraneSluzby) {
                String serviceNameFixed = service.getFriendly().replace("\n", "").replace("\r", "");
                String vybranaSluzbaFixed = vybranaSluzba.replace("\n", "").replace("\r", "");
                if (vybranaSluzbaFixed.equals(serviceNameFixed)) {
                    for (Attribute attribute : service.getAttributesInput()) {
                        result.add(attribute);
                    }
                }
            }
        }
        List<Attribute> resultNew = new ArrayList();
        result.forEach(t -> {
            if (!containsAttribute(t, resultNew) && !t.getCamel().equals("duvodOpravnenostiDotazu")) {
                resultNew.add(t);
            }
        });
        return resultNew;
    }

    // remove duplicit entries from attributes
    private boolean containsAttribute(Attribute atribut, List<Attribute> listAtr) {
        long count = listAtr.stream().filter(t -> {
            return Objects.equals(atribut.getCamel(), t.getCamel());
        }).count();
        return count > 0;
    }

    // create request to obtain PDF file 
    private SearchRequest createGetPDFRequest(List<String> vybraneSluzby, MultiValueMap<String, String> formData, Service[] services, List<Attribute> formValues) throws ParseException {
        SearchRequest result = new SearchRequest();

        // fill service names
        List<String> serviceName = new ArrayList();
        for (Service service : services) {
            for (String vybranaSluzba : vybraneSluzby) {
                String serviceNameFixed = service.getFriendly().replace("\n", "").replace("\r", "");
                String vybranaSluzbaFixed = vybranaSluzba.replace("\n", "").replace("\r", "");
                if (vybranaSluzbaFixed.equals(serviceNameFixed)) {
                    serviceName.add(service.getName());
                }
            }
        }
        result.setServiceNames(serviceName);

        // basic info
        result.setDatumDotazu(new Date());
        result.setDuvod(formData.getFirst("duvod"));
        result.setSoud(formData.getFirst("soud"));
        result.setZnacka(formData.getFirst("znacka"));
        result.setUzivatel(SecurityContextHolder.getContext().getAuthentication().getName());

        // fill all attributes of the request
        // remove already filled information from source first
        formData.remove("duvod");
        formData.remove("soud");
        formData.remove("znacka");
        formData.remove("uzivatel");
        formData.remove("datumDotazu");
        formData.remove("_csrf");
        // fill input values
        List<AttribRequest> inputValues = new ArrayList();
        for (String theKey : formData.keySet()) {
            AttribRequest atribut = new AttribRequest();
            atribut.setCamel(theKey);
            // change checkboxes from form, values ON/OFF converted to true/false
            if (formData.getFirst(theKey).equals("on")) {
                atribut.setValue("true");
            } else if ((formData.getFirst(theKey).equals("") && theKey.equals("pouzeOtevreneVztahy")) || ((formData.getFirst(theKey).equals("") && theKey.equals("zobrazitCisloUctuAdresuVyplaceni")))) {
                atribut.setValue("false");
            } else {
                if ("datumNarozeni".equals(theKey) || "obdobiVypisuOd".equals(theKey) || "obdobiVypisuDo".equals(theKey)) {
                    String badDateFormat = formData.getFirst(theKey);

                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = (Date) formatter.parse(badDateFormat);

                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    String goodDateFormat = dateFormat.format(date);

                    atribut.setValue(goodDateFormat);
                } else {
                    atribut.setValue(formData.getFirst(theKey));
                }
            }
            // load friendly names
            for (Attribute name : formValues) {
                if (theKey.equals(name.getCamel())) {
                    atribut.setFriendly(name.getFriendly());
                    atribut.setType(name.getType());
                }
            }
            inputValues.add(atribut);
        }
        result.setInputValues(inputValues);
        return result;
    }

    // delete info about saved duvod zadosti from session and html page after displayed
    public void removeVerificationMessageFromSession() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            if (session.getAttribute("ciselnikZmenen") != null || session.getAttribute("ciselnikZmenen") != "") {
                session.removeAttribute("ciselnikZmenen");
            }
        } catch (RuntimeException ex) {
            log.error("No Request: ", ex);
        }
    }
}
