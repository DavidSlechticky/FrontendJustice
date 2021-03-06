package net.atos.justice.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FrontendApplication {

    private static final Logger log = LoggerFactory.getLogger(FrontendApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FrontendApplication.class, args);
    }
}
