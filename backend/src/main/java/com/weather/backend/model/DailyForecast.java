package com.weather.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Daily forecast model.
 * Matches the Android app's DailyForecast class.
 */
public class DailyForecast {
    
    @JsonProperty("date")
    private String date;
    
    @JsonProperty("dayName")
    private String dayName;
    
    @JsonProperty("tempMin")
    private double tempMin;
    
    @JsonProperty("tempMax")
    private double tempMax;
    
    @JsonProperty("tempMorning")
    private double tempMorning;
    
    @JsonProperty("tempDay")
    private double tempDay;
    
    @JsonProperty("tempEvening")
    private double tempEvening;
    
    @JsonProperty("tempNight")
    private double tempNight;
    
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
    
    @JsonProperty("precipitationAmount")
    private double precipitationAmount;
    
    @JsonProperty("uvIndex")
    private int uvIndex;
    
    @JsonProperty("weatherCondition")
    private String weatherCondition;
    
    @JsonProperty("weatherDescription")
    private String weatherDescription;
    
    @JsonProperty("weatherIcon")
    private String weatherIcon;

    // Default constructor
    public DailyForecast() {}

    // Getters and Setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getDayName() { return dayName; }
    public void setDayName(String dayName) { this.dayName = dayName; }
    
    public double getTempMin() { return tempMin; }
    public void setTempMin(double tempMin) { this.tempMin = tempMin; }
    
    public double getTempMax() { return tempMax; }
    public void setTempMax(double tempMax) { this.tempMax = tempMax; }
    
    public double getTempMorning() { return tempMorning; }
    public void setTempMorning(double tempMorning) { this.tempMorning = tempMorning; }
    
    public double getTempDay() { return tempDay; }
    public void setTempDay(double tempDay) { this.tempDay = tempDay; }
    
    public double getTempEvening() { return tempEvening; }
    public void setTempEvening(double tempEvening) { this.tempEvening = tempEvening; }
    
    public double getTempNight() { return tempNight; }
    public void setTempNight(double tempNight) { this.tempNight = tempNight; }
    
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
    
    public double getPrecipitationAmount() { return precipitationAmount; }
    public void setPrecipitationAmount(double precipitationAmount) { this.precipitationAmount = precipitationAmount; }
    
    public int getUvIndex() { return uvIndex; }
    public void setUvIndex(int uvIndex) { this.uvIndex = uvIndex; }
    
    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }
    
    public String getWeatherDescription() { return weatherDescription; }
    public void setWeatherDescription(String weatherDescription) { this.weatherDescription = weatherDescription; }
    
    public String getWeatherIcon() { return weatherIcon; }
    public void setWeatherIcon(String weatherIcon) { this.weatherIcon = weatherIcon; }
}
