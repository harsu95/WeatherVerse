package com.weather.app.data.remote;

import com.weather.app.data.model.LocationResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationApiService {

    @GET("location/search")
    Call<List<LocationResult>> searchLocations(@Query("query") String query);

    @GET("location/autocomplete")
    Call<List<LocationResult>> autocompleteLocations(@Query("query") String query);
}
