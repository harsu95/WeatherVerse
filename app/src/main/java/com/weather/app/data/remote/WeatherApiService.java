package com.weather.app.data.remote;

import com.weather.app.data.model.DailyForecast;
import com.weather.app.data.model.HourlyForecast;
import com.weather.app.data.model.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Weather API Service interface for Retrofit.
 * 
 * For production, replace the backend URL with a real weather API:
 * - OpenWeatherMap: https://api.openweathermap.org/data/2.5/
 * - WeatherAPI: https://api.weatherapi.com/v1/
 * - Tomorrow.io: https://api.tomorrow.io/v4/
 */
public interface WeatherApiService {

    /**
     * Get current weather for a location
     */
    @GET("weather/current")
    Call<WeatherResponse> getCurrentWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude
    );

    /**
     * Get hourly forecast (default 24 hours)
     */
    @GET("weather/forecast/hourly")
    Call<List<HourlyForecast>> getHourlyForecast(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("hours") int hours
    );

    /**
     * Get daily forecast (default 7 days)
     */
    @GET("weather/forecast/daily")
    Call<List<DailyForecast>> getDailyForecast(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("days") int days
    );
}
