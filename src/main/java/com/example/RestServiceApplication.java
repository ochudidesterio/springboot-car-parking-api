package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class RestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestServiceApplication.class, args);
        log.info("++==========================================++");
        log.info("|| CAR PARKING BACKEND STARTED SUCCESSFULLY ||");
        log.info("++==========================================++");
    }

}
