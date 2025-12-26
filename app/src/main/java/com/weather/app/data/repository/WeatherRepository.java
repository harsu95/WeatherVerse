package com.weather.app.data.repository;

import android.util.Log;

import com.weather.app.data.model.DailyForecast;
import com.weather.app.data.model.HourlyForecast;
import com.weather.app.data.model.WeatherResponse;
import com.weather.app.data.remote.WeatherApiService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class WeatherRepository {

    private static final String TAG = "WeatherRepository";
    private final WeatherApiService apiService;

    @Inject
    public WeatherRepository(WeatherApiService apiService) {
        this.apiService = apiService;
    }

    public interface WeatherCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    public void getCurrentWeather(double latitude, double longitude, WeatherCallback<WeatherResponse> callback) {
        apiService.getCurrentWeather(latitude, longitude).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch weather data");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching weather", t);
                callback.onError(t.getMessage());
            }
        });
    }

    public void getHourlyForecast(double latitude, double longitude, int hours, WeatherCallback<List<HourlyForecast>> callback) {
        apiService.getHourlyForecast(latitude, longitude, hours).enqueue(new Callback<List<HourlyForecast>>() {
            @Override
            public void onResponse(Call<List<HourlyForecast>> call, Response<List<HourlyForecast>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch forecast");
                }
            }

            @Override
            public void onFailure(Call<List<HourlyForecast>> call, Throwable t) {
                Log.e(TAG, "Error fetching forecast", t);
                callback.onError(t.getMessage());
            }
        });
    }

    public void getDailyForecast(double latitude, double longitude, int days, WeatherCallback<List<DailyForecast>> callback) {
        apiService.getDailyForecast(latitude, longitude, days).enqueue(new Callback<List<DailyForecast>>() {
            @Override
            public void onResponse(Call<List<DailyForecast>> call, Response<List<DailyForecast>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch daily forecast");
                }
            }

            @Override
            public void onFailure(Call<List<DailyForecast>> call, Throwable t) {
                Log.e(TAG, "Error fetching daily forecast", t);
                callback.onError(t.getMessage());
            }
        });
    }
}
