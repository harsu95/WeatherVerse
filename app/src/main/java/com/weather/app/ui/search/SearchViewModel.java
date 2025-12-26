package com.weather.app.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.weather.app.data.model.LocationResult;
import com.weather.app.data.repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SearchViewModel extends ViewModel {

    private final LocationRepository locationRepository;
    private final MutableLiveData<List<LocationResult>> searchResults = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public SearchViewModel(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public LiveData<List<LocationResult>> getSearchResults() {
        return searchResults;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void searchLocations(String query) {
        isLoading.setValue(true);

        locationRepository.searchLocations(query, new LocationRepository.LocationCallback<List<LocationResult>>() {
            @Override
            public void onSuccess(List<LocationResult> result) {
                searchResults.postValue(result);
                isLoading.postValue(false);
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
            }
        });
    }

    public void selectLocation(LocationResult location) {
        locationRepository.saveSelectedLocation(location);
    }

    public void clearResults() {
        searchResults.setValue(new ArrayList<>());
    }
}
