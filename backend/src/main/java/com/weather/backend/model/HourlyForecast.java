package com.weather.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Hourly forecast model.
 * Matches the Android app's HourlyForecast class.
 */
public class HourlyForecast {
    
    @JsonProperty("time")
    private String time;
    
    @JsonProperty("temperature")
    private double temperature;
    
    @JsonProperty("feelsLike")
    private double feelsLike;
    
    @JsonProperty("humidity")
    private int humidity;
    
    @JsonProperty("windSpeed")
    private double windSpeed;
    
    @JsonProperty("windDirection")
    private int windDirection;
    
    @JsonProperty("cloudCoverage")
    private int cloudCoverage;
    
    @JsonProperty("precipitationProbability")
    private double precipitationProbability;
    
    @JsonProperty("weatherCondition")
    private String weatherCondition;
    
    @JsonProperty("weatherDescription")
    private String weatherDescription;
    
    @JsonProperty("weatherIcon")
    private String weatherIcon;

    // Default constructor
    public HourlyForecast() {}

    // Getters and Setters
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    
    public double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }
    
    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }
    
    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }
    
    public int getWindDirection() { return windDirection; }
    public void setWindDirection(int windDirection) { this.windDirection = windDirection; }
    
    public int getCloudCoverage() { return cloudCoverage; }
    public void setCloudCoverage(int cloudCoverage) { this.cloudCoverage = cloudCoverage; }
    
    public double getPrecipitationProbability() { return precipitationProbability; }
    public void setPrecipitationProbability(double precipitationProbability) { this.precipitationProbability = precipitationProbability; }
    
    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }
    
    public String getWeatherDescription() { return weatherDescription; }
    public void setWeatherDescription(String weatherDescription) { this.weatherDescription = weatherDescription; }
    
    public String getWeatherIcon() { return weatherIcon; }
    public void setWeatherIcon(String weatherIcon) { this.weatherIcon = weatherIcon; }
}
