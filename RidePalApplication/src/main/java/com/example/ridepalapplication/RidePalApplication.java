package com.example.ridepalapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.ridepalapplication.models")
public class RidePalApplication {

    public static void main(String[] args) {
        SpringApplication.run(RidePalApplication.class, args);
    }

}
