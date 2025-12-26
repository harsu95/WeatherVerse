package com.weather.app.ui.dashboard;

import com.weather.app.data.model.HourlyForecast;
import com.weather.app.data.model.WeatherResponse;

import java.util.ArrayList;
import java.util.List;

public class DashboardUiState {
    private boolean isLoading = true;
    private WeatherResponse weather = null;
    private List<HourlyForecast> hourlyForecast = new ArrayList<>();
    private String error = null;

    public DashboardUiState() {}

    public boolean isLoading() {
        return isLoading;
    }

    public DashboardUiState setLoading(boolean loading) {
        this.isLoading = loading;
        return this;
    }

    public WeatherResponse getWeather() {
        return weather;
    }

    public DashboardUiState setWeather(WeatherResponse weather) {
        this.weather = weather;
        return this;
    }

    public List<HourlyForecast> getHourlyForecast() {
        return hourlyForecast;
    }

    public DashboardUiState setHourlyForecast(List<HourlyForecast> hourlyForecast) {
        this.hourlyForecast = hourlyForecast;
        return this;
    }

    public String getError() {
        return error;
    }

    public DashboardUiState setError(String error) {
        this.error = error;
        return this;
    }
}
