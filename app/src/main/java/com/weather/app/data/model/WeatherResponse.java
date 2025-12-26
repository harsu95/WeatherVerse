package com.weather.app.data.model;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("locationName")
    private String locationName;
    
    @SerializedName("latitude")
    private double latitude;
    
    @SerializedName("longitude")
    private double longitude;
    
    @SerializedName("timestamp")
    private String timestamp;
    
    @SerializedName("temperature")
    private double temperature;
    
    @SerializedName("feelsLike")
    private double feelsLike;
    
    @SerializedName("humidity")
    private int humidity;
    
    @SerializedName("pressure")
    private double pressure;
    
    @SerializedName("windSpeed")
    private double windSpeed;
    
    @SerializedName("windDirection")
    private int windDirection;
    
    @SerializedName("windDirectionText")
    private String windDirectionText;
    
    @SerializedName("cloudCoverage")
    private int cloudCoverage;
    
    @SerializedName("visibility")
    private double visibility;
    
    @SerializedName("uvIndex")
    private int uvIndex;
    
    @SerializedName("uvIndexLevel")
    private String uvIndexLevel;
    
    @SerializedName("weatherCondition")
    private String weatherCondition;
    
    @SerializedName("weatherDescription")
    private String weatherDescription;
    
    @SerializedName("weatherIcon")
    private String weatherIcon;
    
    @SerializedName("sunrise")
    private String sunrise;
    
    @SerializedName("sunset")
    private String sunset;
    
    @SerializedName("tempMin")
    private double tempMin;
    
    @SerializedName("tempMax")
    private double tempMax;
    
    @SerializedName("airQualityIndex")
    private int airQualityIndex;
    
    @SerializedName("airQualityLevel")
    private String airQualityLevel;

    // Getters
    public String getLocationName() { return locationName; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getTimestamp() { return timestamp; }
    public double getTemperature() { return temperature; }
    public double getFeelsLike() { return feelsLike; }
    public int getHumidity() { return humidity; }
    public double getPressure() { return pressure; }
    public double getWindSpeed() { return windSpeed; }
    public int getWindDirection() { return windDirection; }
    public String getWindDirectionText() { return windDirectionText; }
    public int getCloudCoverage() { return cloudCoverage; }
    public double getVisibility() { return visibility; }
    public int getUvIndex() { return uvIndex; }
    public String getUvIndexLevel() { return uvIndexLevel; }
    public String getWeatherCondition() { return weatherCondition; }
    public String getWeatherDescription() { return weatherDescription; }
    public String getWeatherIcon() { return weatherIcon; }
    public String getSunrise() { return sunrise; }
    public String getSunset() { return sunset; }
    public double getTempMin() { return tempMin; }
    public double getTempMax() { return tempMax; }
    public int getAirQualityIndex() { return airQualityIndex; }
    public String getAirQualityLevel() { return airQualityLevel; }
}
