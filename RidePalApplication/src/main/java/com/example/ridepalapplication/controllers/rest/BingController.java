package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.dtos.LocationDto;
import com.example.ridepalapplication.helpers.BingApiConsumer;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bing")
public class BingController {
    private final BingApiConsumer bingApiConsumer;

    public BingController(BingApiConsumer bingApiConsumer) {
        this.bingApiConsumer = bingApiConsumer;
    }

    @GetMapping
    public int calculateTravelTime(@RequestBody LocationDto locationDto) throws ParseException {
        String startLocation = locationDto.getStartLocation();
        String endLocation = locationDto.getEndLocation();
        String startCoordinates = bingApiConsumer.extractCoordinates(startLocation);
        String endCoordinates = bingApiConsumer.extractCoordinates(endLocation);
        double time = bingApiConsumer.getTime(startCoordinates, endCoordinates);
        return (int)time * 60;
    }
}
