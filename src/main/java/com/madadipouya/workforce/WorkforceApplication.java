package com.madadipouya.workforce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WorkforceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkforceApplication.class, args);
    }
}
