package com.weather.app.data.repository;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.weather.app.data.model.LocationResult;
import com.weather.app.data.remote.LocationApiService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class LocationRepository {

    private static final String TAG = "LocationRepository";
    private static final String PREF_SELECTED_LOCATION = "selected_location";
    
    private final LocationApiService apiService;
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    @Inject
    public LocationRepository(LocationApiService apiService, SharedPreferences sharedPreferences, Gson gson) {
        this.apiService = apiService;
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    public interface LocationCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    public void searchLocations(String query, LocationCallback<List<LocationResult>> callback) {
        apiService.searchLocations(query).enqueue(new Callback<List<LocationResult>>() {
            @Override
            public void onResponse(Call<List<LocationResult>> call, Response<List<LocationResult>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to search locations");
                }
            }

            @Override
            public void onFailure(Call<List<LocationResult>> call, Throwable t) {
                Log.e(TAG, "Error searching locations", t);
                callback.onError(t.getMessage());
            }
        });
    }

    public void saveSelectedLocation(LocationResult location) {
        String json = gson.toJson(location);
        sharedPreferences.edit()
                .putString(PREF_SELECTED_LOCATION, json)
                .apply();
    }

    public LocationResult getSelectedLocation() {
        String json = sharedPreferences.getString(PREF_SELECTED_LOCATION, null);
        if (json != null) {
            return gson.fromJson(json, LocationResult.class);
        }
        return null;
    }
}
