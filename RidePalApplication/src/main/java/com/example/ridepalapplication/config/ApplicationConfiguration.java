package com.example.ridepalapplication.config;

import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@Configuration()
@EnableTransactionManagement
public class ApplicationConfiguration {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
    @Bean
    public JSONParser getJSONParser(){
        return new JSONParser();
    }
}
