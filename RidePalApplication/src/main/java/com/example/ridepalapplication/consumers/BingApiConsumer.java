package com.example.ridepalapplication.consumers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class BingApiConsumer {

    @Value("${bing.key}")
    private String bingKey;
    private final WebClient webClient;
    private final JSONParser parser;

    public BingApiConsumer(WebClient webClient, JSONParser parser) {
        this.webClient = webClient;
        this.parser = parser;
    }

    public String extractCoordinates(String inputLocation) throws ParseException {
        String url = String.format("https://dev.virtualearth.net/REST/v1/Locations?q=%s&key=%s", inputLocation, bingKey);
        JSONObject locationInfo = getInfo(url);
        JSONArray geo = (JSONArray) locationInfo.get("geocodePoints");
        JSONObject point = (JSONObject) geo.get(0);
        JSONArray coordinates = (JSONArray) point.get("coordinates");
        String lat = coordinates.get(0).toString();
        String lon = coordinates.get(1).toString();
        return lat + "," + lon;
    }

    public double getTime(String startLocation, String endLocation) throws ParseException {
        String url = String.format("https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins=%s&destinations=%s&travelMode=driving&key=%s",
                startLocation, endLocation, bingKey);
        JSONObject routeInfo = getInfo(url);
        JSONArray summary = (JSONArray) routeInfo.get("results");
        JSONObject summaryObject = (JSONObject) summary.get(0);
        return (double) summaryObject.get("travelDuration");
    }

    private JSONObject getInfo(String url) throws ParseException {
        String response = webClient.get().uri(url).retrieve().bodyToMono(String.class).block();
        JSONObject object = (JSONObject) parser.parse(response);
        JSONArray results = (JSONArray) object.get("resourceSets");
        JSONObject result = (JSONObject) results.get(0);
        JSONArray resources = (JSONArray) result.get("resources");
        return (JSONObject) resources.get(0);
    }
}
