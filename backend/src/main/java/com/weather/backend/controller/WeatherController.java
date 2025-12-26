package com.weather.backend.controller;

import com.weather.backend.model.DailyForecast;
import com.weather.backend.model.HourlyForecast;
import com.weather.backend.model.WeatherResponse;
import com.weather.backend.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Weather REST Controller.
 * Provides endpoints for current weather and forecasts.
 */
@RestController
@RequestMapping("/api/v1/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Get current weather for a location.
     * 
     * @param lat Latitude
     * @param lon Longitude
     * @return Current weather data
     */
    @GetMapping("/current")
    public ResponseEntity<WeatherResponse> getCurrentWeather(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon) {
        
        try {
            WeatherResponse weather = weatherService.getCurrentWeather(lat, lon);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            System.err.println("Error fetching current weather: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get hourly forecast.
     * 
     * @param lat Latitude
     * @param lon Longitude
     * @param hours Number of hours (default 24)
     * @return List of hourly forecasts
     */
    @GetMapping("/forecast/hourly")
    public ResponseEntity<List<HourlyForecast>> getHourlyForecast(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam(value = "hours", defaultValue = "24") int hours) {
        
        try {
            List<HourlyForecast> forecasts = weatherService.getHourlyForecast(lat, lon, hours);
            return ResponseEntity.ok(forecasts);
        } catch (Exception e) {
            System.err.println("Error fetching hourly forecast: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get daily forecast.
     * 
     * @param lat Latitude
     * @param lon Longitude
     * @param days Number of days (default 7)
     * @return List of daily forecasts
     */
    @GetMapping("/forecast/daily")
    public ResponseEntity<List<DailyForecast>> getDailyForecast(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam(value = "days", defaultValue = "7") int days) {
        
        try {
            List<DailyForecast> forecasts = weatherService.getDailyForecast(lat, lon, days);
            return ResponseEntity.ok(forecasts);
        } catch (Exception e) {
            System.err.println("Error fetching daily forecast: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
