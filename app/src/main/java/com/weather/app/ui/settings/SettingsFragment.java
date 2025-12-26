package com.weather.app.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.weather.app.R;
import com.weather.app.databinding.FragmentSettingsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupUI();
        loadSettings();
    }

    private void setupUI() {
        // Temperature unit selection
        binding.rgTemperature.setOnCheckedChangeListener((group, checkedId) -> {
            // Save preference
        });

        // Wind speed unit selection
        binding.rgWindSpeed.setOnCheckedChangeListener((group, checkedId) -> {
            // Save preference
        });

        // Notifications toggle
        binding.switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save preference
        });

        // Dark mode toggle
        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save preference (app is always dark mode by design)
        });
    }

    private void loadSettings() {
        // Load saved preferences
        binding.rbCelsius.setChecked(true);
        binding.rbKmh.setChecked(true);
        binding.switchNotifications.setChecked(true);
        binding.switchDarkMode.setChecked(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
