/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.atos.justice.frontend.rest.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.atos.justice.frontend.rest.controller.FrontendRestController;
import net.atos.justice.frontend.rest.model.SearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * prepare request to search engine and retrieve PDF file to store
 *
 * @author A760135
 */
public class GetPdfService {

    private static final Logger log = LoggerFactory.getLogger(FrontendRestController.class);

    private final String searchPdfUrl;
    private final String pdfFolder;

    // PDF saving to local disk 
    String fileNameAndPath = "";

    @Autowired
    private RestTemplate restTemplate;

    public GetPdfService(String searchPdfUrl, String pdfFolder) {
        this.searchPdfUrl = searchPdfUrl;
        this.pdfFolder = pdfFolder;
    }

    public String getPdfFile(String token, SearchRequest getPDFRequest) throws IOException {
        log.debug("Odesilam search request pro ziskani PDF " + new Date());
        System.out.println("Odesilam search request pro ziskani PDF " + new Date());
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity<SearchRequest> entity = new HttpEntity<SearchRequest>(getPDFRequest, headers);
        ResponseEntity<byte[]> result = restTemplate.exchange(searchPdfUrl, HttpMethod.POST, entity, byte[].class);

        // test if the folder for PDF file save exists, create one if false
        Files.createDirectories(Paths.get(pdfFolder));
        // creating unique file name
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        Date dt = new Date();
        fileNameAndPath = "";
        fileNameAndPath = pdfFolder + "/" + "zadost_ze_dne" + "_" + sdf.format(dt) + ".pdf";
        System.out.println("Cesta k ulozeni souboru: " + fileNameAndPath);
        try {
            Files.write(Paths.get(fileNameAndPath), result.getBody());
        } catch (Exception ex) {
            System.out.println("Problem s ulozenim souboru: " + ex);
        }
        log.debug("PDF ziskano " + new Date());
        System.out.println("PDF ziskano " + new Date());
        return fileNameAndPath;
    }

}
