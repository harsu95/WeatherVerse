package com.weather.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Current weather response model.
 * Matches the Android app's WeatherResponse class.
 */
public class WeatherResponse {
    
    @JsonProperty("locationName")
    private String locationName;
    
    @JsonProperty("latitude")
    private double latitude;
    
    @JsonProperty("longitude")
    private double longitude;
    
    @JsonProperty("timestamp")
    private String timestamp;
    
    @JsonProperty("temperature")
    private double temperature;
    
    @JsonProperty("feelsLike")
    private double feelsLike;
    
    @JsonProperty("humidity")
    private int humidity;
    
    @JsonProperty("pressure")
    private double pressure;
    
    @JsonProperty("windSpeed")
    private double windSpeed;
    
    @JsonProperty("windDirection")
    private int windDirection;
    
    @JsonProperty("windDirectionText")
    private String windDirectionText;
    
    @JsonProperty("cloudCoverage")
    private int cloudCoverage;
    
    @JsonProperty("visibility")
    private double visibility;
    
    @JsonProperty("uvIndex")
    private int uvIndex;
    
    @JsonProperty("uvIndexLevel")
    private String uvIndexLevel;
    
    @JsonProperty("weatherCondition")
    private String weatherCondition;
    
    @JsonProperty("weatherDescription")
    private String weatherDescription;
    
    @JsonProperty("weatherIcon")
    private String weatherIcon;
    
    @JsonProperty("sunrise")
    private String sunrise;
    
    @JsonProperty("sunset")
    private String sunset;
    
    @JsonProperty("tempMin")
    private double tempMin;
    
    @JsonProperty("tempMax")
    private double tempMax;
    
    @JsonProperty("airQualityIndex")
    private int airQualityIndex;
    
    @JsonProperty("airQualityLevel")
    private String airQualityLevel;

    // Default constructor
    public WeatherResponse() {}

    // Getters and Setters
    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    
    public double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }
    
    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }
    
    public double getPressure() { return pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }
    
    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }
    
    public int getWindDirection() { return windDirection; }
    public void setWindDirection(int windDirection) { this.windDirection = windDirection; }
    
    public String getWindDirectionText() { return windDirectionText; }
    public void setWindDirectionText(String windDirectionText) { this.windDirectionText = windDirectionText; }
    
    public int getCloudCoverage() { return cloudCoverage; }
    public void setCloudCoverage(int cloudCoverage) { this.cloudCoverage = cloudCoverage; }
    
    public double getVisibility() { return visibility; }
    public void setVisibility(double visibility) { this.visibility = visibility; }
    
    public int getUvIndex() { return uvIndex; }
    public void setUvIndex(int uvIndex) { this.uvIndex = uvIndex; }
    
    public String getUvIndexLevel() { return uvIndexLevel; }
    public void setUvIndexLevel(String uvIndexLevel) { this.uvIndexLevel = uvIndexLevel; }
    
    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }
    
    public String getWeatherDescription() { return weatherDescription; }
    public void setWeatherDescription(String weatherDescription) { this.weatherDescription = weatherDescription; }
    
    public String getWeatherIcon() { return weatherIcon; }
    public void setWeatherIcon(String weatherIcon) { this.weatherIcon = weatherIcon; }
    
    public String getSunrise() { return sunrise; }
    public void setSunrise(String sunrise) { this.sunrise = sunrise; }
    
    public String getSunset() { return sunset; }
    public void setSunset(String sunset) { this.sunset = sunset; }
    
    public double getTempMin() { return tempMin; }
    public void setTempMin(double tempMin) { this.tempMin = tempMin; }
    
    public double getTempMax() { return tempMax; }
    public void setTempMax(double tempMax) { this.tempMax = tempMax; }
    
    public int getAirQualityIndex() { return airQualityIndex; }
    public void setAirQualityIndex(int airQualityIndex) { this.airQualityIndex = airQualityIndex; }
    
    public String getAirQualityLevel() { return airQualityLevel; }
    public void setAirQualityLevel(String airQualityLevel) { this.airQualityLevel = airQualityLevel; }
}
