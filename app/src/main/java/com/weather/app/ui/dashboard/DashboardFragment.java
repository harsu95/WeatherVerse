package com.weather.app.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.weather.app.R;
import com.weather.app.databinding.FragmentDashboardBinding;
import com.weather.app.location.LocationPermissionHelper;
import com.weather.app.location.LocationService;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel viewModel;
    private HourlyForecastAdapter hourlyAdapter;

    @Inject
    LocationService locationService;

    private final ActivityResultLauncher<String[]> locationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean fineLocationGranted = result.getOrDefault(
                        android.Manifest.permission.ACCESS_FINE_LOCATION, false);
                boolean coarseLocationGranted = result.getOrDefault(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION, false);

                if (fineLocationGranted || coarseLocationGranted) {
                    fetchCurrentLocation();
                } else {
                    Toast.makeText(requireContext(), 
                            "Location permission needed for accurate weather", 
                            Toast.LENGTH_SHORT).show();
                    // Load default location
                    viewModel.loadWeather(37.7749, -122.4194);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        setupUI();
        setupRecyclerView();
        observeViewModel();

        // Request location and load weather
        checkLocationAndLoadWeather();
    }

    private void checkLocationAndLoadWeather() {
        if (LocationPermissionHelper.hasLocationPermission(requireContext())) {
            fetchCurrentLocation();
        } else {
            LocationPermissionHelper.requestPermissions(locationPermissionLauncher);
        }
    }

    private void fetchCurrentLocation() {
        locationService.getCurrentLocation(new LocationService.LocationListener() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                viewModel.loadWeather(latitude, longitude);
            }

            @Override
            public void onLocationError(String error) {
                Toast.makeText(requireContext(), 
                        "Using default location", 
                        Toast.LENGTH_SHORT).show();
                // Fall back to San Francisco
                viewModel.loadWeather(37.7749, -122.4194);
            }
        });
    }

    private void setupUI() {
        binding.btnSettings.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.settingsFragment)
        );

        binding.tvLocation.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_dashboard_to_search)
        );

        binding.swipeRefresh.setOnRefreshListener(() -> {
            checkLocationAndLoadWeather();
        });

        binding.swipeRefresh.setColorSchemeResources(R.color.primary);
    }

    private void setupRecyclerView() {
        hourlyAdapter = new HourlyForecastAdapter();
        binding.rvHourlyForecast.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rvHourlyForecast.setAdapter(hourlyAdapter);
    }

    private void observeViewModel() {
        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            binding.swipeRefresh.setRefreshing(state.isLoading());

            if (state.getWeather() != null) {
                updateWeatherUI(state);
            }

            if (state.getHourlyForecast() != null) {
                hourlyAdapter.submitList(state.getHourlyForecast());
            }

            if (state.getError() != null && !state.isLoading()) {
                Toast.makeText(requireContext(), state.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateWeatherUI(DashboardUiState state) {
        // Location
        binding.tvLocation.setText(state.getWeather().getLocationName());
        binding.tvDate.setText("Today");

        // Temperature
        String temp = String.format("%.0f°", state.getWeather().getTemperature());
        binding.tvTemperature.setText(temp);
        binding.tvCondition.setText(state.getWeather().getWeatherCondition());

        String high = String.format("H: %.0f°", state.getWeather().getTempMax());
        String low = String.format("L: %.0f°", state.getWeather().getTempMin());
        binding.tvHighLow.setText(high + "  " + low);

        // Metrics
        binding.cardHumidity.tvValue.setText(state.getWeather().getHumidity() + "%");
        binding.cardPressure.tvValue.setText(String.format("%.0f", state.getWeather().getPressure()));
        binding.cardWind.tvValue.setText(String.format("%.0f", state.getWeather().getWindSpeed()));
        binding.cardUv.tvValue.setText(state.getWeather().getUvIndexLevel());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
