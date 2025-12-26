package com.weather.app.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.weather.app.data.model.HourlyForecast;
import com.weather.app.data.model.WeatherResponse;
import com.weather.app.data.repository.WeatherRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DashboardViewModel extends ViewModel {

    private final WeatherRepository weatherRepository;
    private final MutableLiveData<DashboardUiState> uiState = new MutableLiveData<>(new DashboardUiState());

    @Inject
    public DashboardViewModel(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public LiveData<DashboardUiState> getUiState() {
        return uiState;
    }

    public void loadWeather(double latitude, double longitude) {
        DashboardUiState currentState = uiState.getValue();
        if (currentState != null) {
            uiState.setValue(currentState.setLoading(true).setError(null));
        }

        weatherRepository.getCurrentWeather(latitude, longitude, new WeatherRepository.WeatherCallback<WeatherResponse>() {
            @Override
            public void onSuccess(WeatherResponse result) {
                DashboardUiState state = uiState.getValue();
                if (state != null) {
                    uiState.postValue(state.setLoading(false).setWeather(result));
                }
            }

            @Override
            public void onError(String error) {
                DashboardUiState state = uiState.getValue();
                if (state != null) {
                    uiState.postValue(state.setLoading(false).setError(error));
                }
            }
        });

        weatherRepository.getHourlyForecast(latitude, longitude, 24, new WeatherRepository.WeatherCallback<List<HourlyForecast>>() {
            @Override
            public void onSuccess(List<HourlyForecast> result) {
                DashboardUiState state = uiState.getValue();
                if (state != null) {
                    uiState.postValue(state.setHourlyForecast(result));
                }
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }

    public void refresh() {
        WeatherResponse weather = uiState.getValue() != null ? uiState.getValue().getWeather() : null;
        if (weather != null) {
            loadWeather(weather.getLatitude(), weather.getLongitude());
        }
    }
}
