package com.example.ridepalapplication.helpers;

import com.example.ridepalapplication.dtos.LocationDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class BingApiConsumer {

    private static final String BING_KEY = "Ala7uM9ba62lN6JsHjAOf_-q-eNAoNfZdztFvccxqZs6DT8Ttaw-1oYNc9sKXZbW";

    private final RestTemplate restTemplate;
    private final JSONParser parser;

    public BingApiConsumer(RestTemplate restTemplate, JSONParser parser) {
        this.restTemplate = restTemplate;
        this.parser = parser;
    }

    public String extractCoordinates(String inputLocation) throws ParseException {
//        String url = String.format("https://dev.virtualearth.net/REST/v1/Locations?query=%s&key=%s",inputLocation, BING_KEY);
        String url= String.format("https://dev.virtualearth.net/REST/v1/Locations?q=%s&key=%s", inputLocation, BING_KEY);
        String response = restTemplate.getForObject(url, String.class);
        JSONObject object = (JSONObject) parser.parse(response);
        JSONArray results = (JSONArray) object.get("resourceSets");
        JSONObject result = (JSONObject) results.get(0);
        JSONArray locations = (JSONArray) result.get("resources");
        JSONObject location = (JSONObject) locations.get(0);
        JSONArray geo = (JSONArray) location.get("geocodePoints");
        JSONObject point = (JSONObject) geo.get(0);
        JSONArray coordinates = (JSONArray) point.get("coordinates");
        String lat = coordinates.get(0).toString();
        String lon = coordinates.get(1).toString();
        return lat + "," + lon;
    }

    public double getTime(String startLocation, String endLocation) throws ParseException {
        String url = String.format("https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins=%s&destinations=%s&travelMode=driving&key=%s",
                startLocation, endLocation, BING_KEY);
        String response = restTemplate.getForObject(url, String.class);
        JSONObject object = (JSONObject) parser.parse(response);
        JSONArray results = (JSONArray) object.get("resourceSets");
        JSONObject result = (JSONObject) results.get(0);
        JSONArray routes = (JSONArray) result.get("resources");
        JSONObject route = (JSONObject) routes.get(0);
        JSONArray summary = (JSONArray) route.get("results");
        JSONObject summaryObject = (JSONObject) summary.get(0);
        return (double) summaryObject.get("travelDuration");
    }
}
