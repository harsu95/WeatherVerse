package com.weather.app.ui.forecast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.weather.app.databinding.FragmentForecastBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ForecastFragment extends Fragment {

    private FragmentForecastBinding binding;
    private ForecastViewModel viewModel;
    private DailyForecastAdapter dailyAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentForecastBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ForecastViewModel.class);

        setupRecyclerView();
        observeViewModel();

        // Load forecast data
        viewModel.loadForecast(37.7749, -122.4194);
    }

    private void setupRecyclerView() {
        dailyAdapter = new DailyForecastAdapter();
        binding.rvDailyForecast.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );
        binding.rvDailyForecast.setAdapter(dailyAdapter);
    }

    private void observeViewModel() {
        viewModel.getDailyForecast().observe(getViewLifecycleOwner(), forecast -> {
            if (forecast != null) {
                dailyAdapter.submitList(forecast);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
