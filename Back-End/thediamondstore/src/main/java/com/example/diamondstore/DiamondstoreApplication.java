package com.example.diamondstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DiamondstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiamondstoreApplication.class, args);
    }

}
