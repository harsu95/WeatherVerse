package com.weather.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class DailyForecast {
    @SerializedName("date")
    private String date;

    @SerializedName("dayName")
    private String dayName;

    @SerializedName("tempMin")
    private double tempMin;

    @SerializedName("tempMax")
    private double tempMax;

    @SerializedName("tempMorning")
    private double tempMorning;

    @SerializedName("tempDay")
    private double tempDay;

    @SerializedName("tempEvening")
    private double tempEvening;

    @SerializedName("tempNight")
    private double tempNight;

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

    @SerializedName("precipitationAmount")
    private double precipitationAmount;

    @SerializedName("uvIndex")
    private int uvIndex;

    @SerializedName("weatherCondition")
    private String weatherCondition;

    @SerializedName("weatherDescription")
    private String weatherDescription;

    @SerializedName("weatherIcon")
    private String weatherIcon;

    // Getters
    public String getDate() { return date; }
    public String getDayName() { return dayName; }
    public double getTempMin() { return tempMin; }
    public double getTempMax() { return tempMax; }
    public double getTempMorning() { return tempMorning; }
    public double getTempDay() { return tempDay; }
    public double getTempEvening() { return tempEvening; }
    public double getTempNight() { return tempNight; }
    public int getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    public int getWindDirection() { return windDirection; }
    public int getCloudCoverage() { return cloudCoverage; }
    public double getPrecipitationProbability() { return precipitationProbability; }
    public double getPrecipitationAmount() { return precipitationAmount; }
    public int getUvIndex() { return uvIndex; }
    public String getWeatherCondition() { return weatherCondition; }
    public String getWeatherDescription() { return weatherDescription; }
    public String getWeatherIcon() { return weatherIcon; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyForecast that = (DailyForecast) o;
        return Objects.equals(date, that.date) &&
               Double.compare(that.tempMax, tempMax) == 0 &&
               Double.compare(that.tempMin, tempMin) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, tempMin, tempMax);
    }
}
