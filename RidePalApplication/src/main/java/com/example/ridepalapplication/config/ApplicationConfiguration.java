package com.example.ridepalapplication.config;

import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public WebClient webClient(){
        return WebClient.builder().build();
    }
    @Bean
    public JSONParser getJSONParser(){
        return new JSONParser();
    }
}
