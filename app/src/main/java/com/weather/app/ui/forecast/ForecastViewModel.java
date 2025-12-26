package com.weather.app.ui.forecast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.weather.app.data.model.DailyForecast;
import com.weather.app.data.repository.WeatherRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ForecastViewModel extends ViewModel {

    private final WeatherRepository weatherRepository;
    private final MutableLiveData<List<DailyForecast>> dailyForecast = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public ForecastViewModel(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public LiveData<List<DailyForecast>> getDailyForecast() {
        return dailyForecast;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadForecast(double latitude, double longitude) {
        isLoading.setValue(true);

        weatherRepository.getDailyForecast(latitude, longitude, 7, 
            new WeatherRepository.WeatherCallback<List<DailyForecast>>() {
                @Override
                public void onSuccess(List<DailyForecast> result) {
                    dailyForecast.postValue(result);
                    isLoading.postValue(false);
                }

                @Override
                public void onError(String error) {
                    isLoading.postValue(false);
                }
            }
        );
    }
}
