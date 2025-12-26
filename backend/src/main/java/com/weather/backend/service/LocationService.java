package com.weather.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weather.backend.model.LocationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Location service that uses Open-Meteo Geocoding API.
 * Free and no API key required!
 */
@Service
public class LocationService {

    private final WebClient webClient;
    
    @Value("${openmeteo.api.geocoding}")
    private String geocodingUrl;

    public LocationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Search for locations by name
     */
    public List<LocationResult> searchLocations(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String url = geocodingUrl + "?name=" + query + "&count=10&language=en&format=json";

        try {
            JsonNode response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            return mapToLocationResults(response);
        } catch (Exception e) {
            System.err.println("Error searching locations: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Autocomplete locations (same as search but for typeahead)
     */
    public List<LocationResult> autocompleteLocations(String query) {
        if (query == null || query.length() < 2) {
            return new ArrayList<>();
        }

        String url = geocodingUrl + "?name=" + query + "&count=5&language=en&format=json";

        try {
            JsonNode response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            return mapToLocationResults(response);
        } catch (Exception e) {
            System.err.println("Error autocompleting locations: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<LocationResult> mapToLocationResults(JsonNode response) {
        List<LocationResult> results = new ArrayList<>();

        if (response == null || !response.has("results")) {
            return results;
        }

        JsonNode resultsNode = response.path("results");
        int order = 0;
        
        for (JsonNode node : resultsNode) {
            LocationResult location = new LocationResult();
            
            location.setId(String.valueOf(node.path("id").asLong()));
            location.setName(node.path("name").asText());
            location.setLatitude(node.path("latitude").asDouble());
            location.setLongitude(node.path("longitude").asDouble());
            location.setCountry(node.path("country").asText(""));
            location.setState(node.path("admin1").asText("")); // admin1 is state/province
            location.setTimezone(node.path("timezone").asText("UTC"));
            location.setPrimary(order == 0);
            location.setDisplayOrder(order++);
            
            results.add(location);
        }

        return results;
    }
}
