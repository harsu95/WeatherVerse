package com.weather.backend.controller;

import com.weather.backend.model.LocationResult;
import com.weather.backend.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Location REST Controller.
 * Provides endpoints for location search and autocomplete.
 */
@RestController
@RequestMapping("/api/v1/location")
@CrossOrigin(origins = "*")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Search for locations by name.
     * 
     * @param query Search query
     * @return List of matching locations
     */
    @GetMapping("/search")
    public ResponseEntity<List<LocationResult>> searchLocations(
            @RequestParam("query") String query) {
        
        try {
            List<LocationResult> locations = locationService.searchLocations(query);
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            System.err.println("Error searching locations: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Autocomplete locations for typeahead.
     * 
     * @param query Partial search query
     * @return List of matching locations (limited)
     */
    @GetMapping("/autocomplete")
    public ResponseEntity<List<LocationResult>> autocompleteLocations(
            @RequestParam("query") String query) {
        
        try {
            List<LocationResult> locations = locationService.autocompleteLocations(query);
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            System.err.println("Error autocompleting locations: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
