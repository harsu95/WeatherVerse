package com.weather.app.ui.alerts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.weather.app.databinding.FragmentAlertsBinding;

import dagger.hilt.android.AndroidEntryPoint;

import java.util.ArrayList;
import java.util.List;

@AndroidEntryPoint
public class AlertsFragment extends Fragment {

    private FragmentAlertsBinding binding;
    private AlertsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAlertsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupRecyclerView();
        loadAlerts();
    }

    private void setupRecyclerView() {
        adapter = new AlertsAdapter();
        binding.rvAlerts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvAlerts.setAdapter(adapter);
    }

    private void loadAlerts() {
        // Mock alerts data
        List<WeatherAlert> alerts = new ArrayList<>();
        alerts.add(new WeatherAlert(
            "1",
            "Heat Advisory",
            "High temperatures expected. Stay hydrated and avoid prolonged sun exposure.",
            "warning",
            "Active until 8:00 PM"
        ));
        alerts.add(new WeatherAlert(
            "2",
            "Air Quality Alert",
            "Moderate air quality due to wildfire smoke. Sensitive groups should limit outdoor activity.",
            "caution",
            "Active until Tomorrow"
        ));
        
        adapter.submitList(alerts);
        
        // Show/hide empty state
        binding.tvNoAlerts.setVisibility(alerts.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
