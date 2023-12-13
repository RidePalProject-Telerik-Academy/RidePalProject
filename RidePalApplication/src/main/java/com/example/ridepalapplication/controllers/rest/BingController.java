package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.dtos.LocationDto;
import com.example.ridepalapplication.consumers.BingApiConsumer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bing")
@Tag(name = "Bing")
public class BingController {
    private final BingApiConsumer bingApiConsumer;

    public BingController(BingApiConsumer bingApiConsumer) {
        this.bingApiConsumer = bingApiConsumer;
    }

    @Operation(summary = "Connect to Bing API")
    @GetMapping
    public int calculateTravelTime(@RequestBody LocationDto locationDto) throws ParseException {
        String startCoordinates = bingApiConsumer.extractCoordinates(locationDto.getStartLocation()+ "," + locationDto.getStartAddress());
        String endCoordinates = bingApiConsumer.extractCoordinates(locationDto.getEndLocation()+ "," + locationDto.getEndAddress());
        double time = bingApiConsumer.getTime(startCoordinates, endCoordinates);
        return (int)time * 60;
    }
}
