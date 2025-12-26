package com.weather.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for WeatherVerse Backend.
 * Provides REST API endpoints for the WeatherVerse Android app.
 * Uses Open-Meteo API (free, no API key required) for weather data.
 */
@SpringBootApplication
public class WeatherBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherBackendApplication.class, args);
        System.out.println("========================================");
        System.out.println("  WeatherVerse Backend Started!");
        System.out.println("  Server: http://localhost:8080");
        System.out.println("  API Base: http://localhost:8080/api/v1/");
        System.out.println("========================================");
    }
}
