package com.weather.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class HourlyForecast {
    @SerializedName("time")
    private String time;
    
    @SerializedName("temperature")
    private double temperature;
    
    @SerializedName("feelsLike")
    private double feelsLike;
    
    @SerializedName("humidity")
    private int humidity;
    
    @SerializedName("windSpeed")
    private double windSpeed;
    
    @SerializedName("windDirection")
    private int windDirection;
    
    @SerializedName("cloudCoverage")
    private int cloudCoverage;
    
    @SerializedName("precipitationProbability")
    private double precipitationProbability;
    
    @SerializedName("weatherCondition")
    private String weatherCondition;
    
    @SerializedName("weatherDescription")
    private String weatherDescription;
    
    @SerializedName("weatherIcon")
    private String weatherIcon;

    // Getters
    public String getTime() { return time; }
    public double getTemperature() { return temperature; }
    public double getFeelsLike() { return feelsLike; }
    public int getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    public int getWindDirection() { return windDirection; }
    public int getCloudCoverage() { return cloudCoverage; }
    public double getPrecipitationProbability() { return precipitationProbability; }
    public String getWeatherCondition() { return weatherCondition; }
    public String getWeatherDescription() { return weatherDescription; }
    public String getWeatherIcon() { return weatherIcon; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HourlyForecast that = (HourlyForecast) o;
        return Double.compare(that.temperature, temperature) == 0 &&
               Objects.equals(time, that.time) &&
               Objects.equals(weatherCondition, that.weatherCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, temperature, weatherCondition);
    }
}
